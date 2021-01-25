from psycopg2 import *
from psycopg2 import sql
from psycopg2._psycopg import AsIs, quote_ident


class Database:
    conn = ''

    # Create connection to database
    def connect(self):
        self.conn = connect(database="tweb", user="tweb", password="tweb", host="127.0.0.1", port="5432")
        print("Database opened successfully")

    # Get Col names in table
    def getColNames(self, table_name):
        self.connect()
        cur = self.conn.cursor()
        # if table_name == "corso_docente":
        #     cur.execute('''
        #         SELECT cd.id, cs.titolo, d.nome , d.cognome
        #         from corso_docente as cd join corso as cs on  cd.corso = cs.id
        #         join docente as d on cd.docente = d.id LIMIT 0
        #     ''')
        # else:
        #     cur.execute("SELECT * FROM " + table_name + " Limit 0")
        cur.execute("SELECT * FROM " + table_name + " Limit 0")
        col_names = [desc[0] for desc in cur.description]
        self.conn.close()
        return col_names

    def getTableData(self, table_name):
        self.connect()
        cur = self.conn.cursor()
        # if table_name == "corso_docente":
        #     cur.execute('''
        #         SELECT cd.id, cs.titolo, d.nome , d.cognome
        #         from corso_docente as cd join corso as cs on  cd.corso = cs.id
        #         join docente as d on cd.docente = d.id
        #     ''')
        # else:
        #     cur.execute("SELECT * FROM " + table_name)
        cur.execute("SELECT * FROM " + table_name)
        res = cur.fetchall()
        return res

    def addRecord(self, table_name, values):
        self.connect()
        cur = self.conn.cursor()
        sql_statement = "INSERT INTO %s VALUES (DEFAULT, " + self.place_holder(
            values) + ") RETURNING id"
        print(sql_statement)
        cur.execute(sql_statement, (AsIs(table_name), *values,))
        index = cur.fetchone()
        self.conn.commit()
        self.conn.close()
        return self.getRecord(table_name, index)

    def getRecord(self, table_name, index):
        self.connect()
        cur = self.conn.cursor()
        cur.execute("SELECT * FROM %s WHERE id = %s", (AsIs(table_name), index))
        return cur.fetchone()

    def deleteRecord(self, table_name, index):
        self.connect()
        cur = self.conn.cursor()
        cur.execute("DELETE FROM %s WHERE id = %s", (AsIs(table_name), index))
        self.conn.commit()
        self.conn.close()

    def updateRecord(self, table_name, values, index):
        headers = self.getColNames(table_name)[1:]
        self.connect()
        cur = self.conn.cursor()
        sql_statement = "Update %s SET (" + self.place_holder(headers) + ") =" \
                        " (" + self.place_holder(values) + ") WHERE id = %s"
        cur.execute(sql_statement, (AsIs(table_name), *[AsIs(item) for item in headers], *values, index,))
        self.conn.commit()
        self.conn.close()

        return self.getRecord(table_name, index)

    def place_holder(self, values):
        return '{}'.format(', '.join('%s' for i in values))



# db = Database()

# print(db.addSubject("Test15", False))
# db.deleteSubject(100)
# print(db.updateSubject(106, "pogchamp12", "True"))
# print(*range(len(db.getColNames("prenotazione"))-1))
# print(db.getTableData("user_session"))
# values = (41, 99)
# db.updateRecord('corso_docente', values, 159)
# print(db.getRecord('corso_docente', 159))
# print(db.place_holder(values))
