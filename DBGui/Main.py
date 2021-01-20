from tkinter import *
from tkinter import ttk
from Database import *

db = Database()
root = Tk()

# Subjects tab
# TreeView Frame
frame = Frame(root)
tree = ttk.Treeview(frame)

tree["columns"] = ("ID", "Name", "Deleted")
tree.column("#0", width=0, stretch=NO)
tree.column("ID", anchor=CENTER, width=80)
tree.column("Name", anchor=W, width=120)
tree.column("Deleted", anchor=W, width=120)

tree.heading("#0", text="Label", anchor=W)
tree.heading("ID", text="ID", anchor=CENTER)
tree.heading("Name", text="Name", anchor=W)
tree.heading("Deleted", text="Deleted", anchor=W)

global count
# Idk why this error but it works lmao
count = 0
for item in db.getSubjects():
    tree.insert(parent='', index='end', iid=count, text="", values=item)
    count += 1


def select(a):
    value = tree.focus()
    print(tree.item(value, 'values'))
    name_box.delete(0, END)
    name_box.insert(value, tree.item(value, 'values')[1])
    var.set(tree.item(value, 'values')[2])


tree.bind('<<TreeviewSelect>>', select)

tree.pack()
frame.pack()


# Functions
def addRecord():
    global count
    try:
        res = db.addSubject(name_box.get(), var.get())
        print(res)
        tree.insert(parent='', index='end', iid=count, text="", values=(res[0], res[1], res[2]))
        name_box.delete(0, END)
        count += 1
    except Error:
        print("Error")


def deleteRecord():
    global count
    try:
        value = tree.item(tree.selection()[0], 'values')[0]
        db.deleteSubject(value)
        tree.delete(tree.selection()[0])
    except Error:
        print("Error")


# todo
def updateRecord():
    global count
    value = tree.item(tree.selection()[0], 'values')[0]
    updated = db.updateSubject(value, name_box.get(), var.get())
    print(updated)
    tree.insert(parent="", index=tree.selection()[0], iid=count, values=updated)
    tree.delete(tree.selection()[0])


# Editbox frame
edit_frame = Frame(root)
edit_frame.pack(pady=20)

name_label = Label(edit_frame, text="Name")
name_label.grid(row=0, column=0)

deleted_label = Label(edit_frame, text="Deleted")
deleted_label.grid(row=0, column=1)

name_box = Entry(edit_frame)
name_box.grid(row=1, column=0)

var = BooleanVar()
var.set("False")
deleted_menu = OptionMenu(edit_frame, var, "True", "False")
deleted_menu.grid(row=1, column=1)

# buttons frame
buttons_frame = Frame(root)
buttons_frame.pack(pady=20)

add_button = Button(buttons_frame, text="Add record", command=addRecord)
add_button.pack(pady=20)

delete_button = Button(buttons_frame, text="Delete record", command=deleteRecord)
delete_button.pack(pady=20)

update_button = Button(buttons_frame, text="Update record", command=updateRecord)
update_button.pack(pady=20)

root.mainloop()
