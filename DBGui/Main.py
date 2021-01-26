from tkinter import *
from tkinter import ttk
from tkinter import font as tk
from Database import *

db = Database()
root = Tk()
root.geometry("500x500")

# Subjects tab
# TreeView Frame


tree_frame = Frame(root)
field_frame = Frame(root)
button_frame = Frame(root)

# list containing entries
global entry_list
entry_list = []

# list containing cols sizes
global col_sizes
col_sizes = []


# dynamically creates entries
def createFields(table_name):
    global entry_list
    cols = db.getColNames(table_name)

    length = len(cols)
    for x in range(length - 1):
        entry = Entry(field_frame)
        entry.grid(row=0, column=x, pady=10)
        entry_list.append(entry)



def createTreeView(table_name):
    tree = ttk.Treeview(tree_frame)
    cols = db.getColNames(table_name)
    tree.heading("#0", text="Label", anchor=W)
    tree["columns"] = tuple(cols)
    tree.column("#0", width=0, stretch=False)
    for item in cols:
        tree.column(item, anchor=W, minwidth=80, stretch=True)
        tree.heading(item, text=item, anchor=W)

    global count
    count = 0
    for item in db.getTableData(table_name):
        tree.insert(parent='', index='end', iid=count, text="", values=item)
        count += 1

    def select(a):
        value = tree.focus()
        print(tree.item(value, 'values'))
        index = 1
        for entry in entry_list:
            entry.delete(0, END)
            entry.insert(value, tree.item(value, 'values')[index])
            index += 1

    tree.bind('<<TreeviewSelect>>', select)

    tree.pack(fill='x')
    return tree


tree_frame.pack()
field_frame.pack()
button_frame.pack()


def itemSelected(a):
    global tree, entry_list
    if tree is not None:
        tree.destroy()
    tree = createTreeView(str_var.get())
    for item in entry_list:
        item.destroy()
    entry_list.clear()
    createFields(str_var.get())


table_names = ["corso", "corso_docente", "docente", "prenotazione", "user_session", "utente"]
str_var = StringVar()
str_var.set(table_names[0])
table_menu = OptionMenu(tree_frame, str_var, *table_names, command=itemSelected)
table_menu.pack()

global tree
tree = createTreeView(str_var.get())
createFields(str_var.get())


def addRecord():
    global count
    new_record = db.addRecord(str_var.get(), [item.get() for item in entry_list])
    tree.insert(parent='', index='end', iid=count, text='', values=new_record)
    for entry in entry_list:
        entry.delete(0, END)


def deleteRecord():
    value = tree.item(tree.selection()[0], 'values')[0]
    db.deleteRecord(str_var.get(), value)
    tree.delete(tree.selection()[0])
    for entry in entry_list:
        entry.delete(0, END)


def updateRecord():
    value = tree.item(tree.selection()[0], 'values')[0]
    updated_record = db.updateRecord(str_var.get(), [item.get() for item in entry_list], value)
    tree.insert(parent='', index=tree.selection()[0], iid=count, text='', values=updated_record)
    tree.delete(tree.selection()[0])
    for entry in entry_list:
        entry.delete(0, END)


add_button = Button(button_frame, text="Add record", command=addRecord)
add_button.pack()

delete_button = Button(button_frame, text="Delete record", command=deleteRecord)
delete_button.pack()

update_button = Button(button_frame, text="Update record", command=updateRecord)
update_button.pack()

root.mainloop()
