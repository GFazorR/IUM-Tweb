from Filetto import *
from tkinter import *
from tkinter import messagebox

global buttons, \
    turn, \
    players, \
    label_player_list, \
    label_size, \
    game_config, \
    game_frame, \
    score_board_frame, \
    scoreboard, \
    scoreboard_labels
buttons = []
turn = 0
players = []
scoreboard = []
scoreboard_labels = []


def resetGame():
    global buttons, players, turn, label_player_list, scoreboard_labels
    for item in buttons:
        item.configure(text="")
    score_board_frame.destroy()
    scoreboard.clear()
    scoreboard_labels.clear()
    buttons.clear()
    print(buttons)
    players.clear()
    turn = 0
    label_player_list.configure(text="")


def continueGame():
    global turn, buttons, scoreboard, scoreboard_labels
    turn = 0
    buttons.clear()
    scoreboard_labels.clear()
    createGame(game_frame)


def setGridSize(entry_size):
    global label_size
    setSize(int(entry_size.get()))
    label_size.configure(text=entry_size.get())
    entry_size.delete(0, END)


def addPlayer(entry_player):
    global players, label_player_list
    players.append(entry_player.get())
    label_player_list.configure(text=players)
    entry_player.delete(0, END)


def click(button):
    global turn, players, scoreboard
    player_symbol = players[turn % len(players)]
    if button["text"] == '':
        button["text"] = player_symbol
        info = button.grid_info()
        addItem(player_symbol, info["row"], info["column"])
        score = calcScore(player_symbol, filetto_grid)
        scoreboard[turn % len(scoreboard)] += score
        scoreboard_labels[turn % len(scoreboard_labels)] \
            .configure(text=player_symbol + " Score: " + str(score))
        if score >= 50:
            response = messagebox.askyesno("Player " + player_symbol + " Wins",
                                           "Reset game?")
            if response == 1:
                continueGame()
            else:
                resetGame()

        elif gameEnd(filetto_grid):
            response = messagebox.askyesno("Draw",
                                           "Reset game?")
            if response == 1:
                continueGame()
            else:
                resetGame()

        else:
            turn += 1


def createGame(game_frame):
    global buttons, players, turn
    if not players:
        messagebox.showerror("Error, no players in game.", "Add players to start the game")
    else:
        if buttons:
            buttons.clear()
            turn = 0
            print(buttons)
        count = 0
        createGrid()
        for col in range(getSize()):
            for row in range(getSize()):
                buttons.append(Button(game_frame,
                                      text='',
                                      font=("Helvetica", 20),
                                      height=1,
                                      width=2,
                                      command=lambda i=count: click(buttons[i])))
                buttons[count].grid(row=row, column=col)

                count += 1
        game_frame.pack()
        createScoreboard()


def createScoreboard():
    global players, scoreboard, scoreboard_labels, score_board_frame
    if score_board_frame is not None:
        score_board_frame = Frame(root)
    scoreboard = []
    count = 0
    for player in players:
        scoreboard.append(0)
        label = Label(score_board_frame, text=player + " Score: 0", width=10)
        scoreboard_labels.append(label)
        label.grid(row=0, column=count)
        count += 1
    print(scoreboard_labels)
    score_board_frame.pack()


def gameConfig(root):
    global game_frame, game_config, score_board_frame, label_player_list, label_size
    game_frame = Frame(root)
    game_config = Frame(root)
    score_board_frame = Frame(root)
    # todo input check,
    # set gameconfig frame
    # add player entry & button
    entry_player = Entry(game_config)
    add_player = Button(game_config, text="Add player",
                        font=("Helvetica", 12),
                        width=10,
                        command=lambda: addPlayer(entry_player))
    # set grid size button
    entry_size = Entry(game_config)
    button_size = Button(game_config, text="Set Grid size",
                         font=("Helvetica", 12),
                         width=10,
                         command=lambda: setGridSize(entry_size))

    # show player list
    label_player_list = Label(game_config, width=10)
    label_list = Label(game_config, width=10, text="Player's list")

    # show current grid size
    label_grid_size = Label(game_config, width=10, text="Grid size")
    label_size = Label(game_config, width=10, text=getSize())

    # start game button
    start = Button(game_config, text="Start Game",
                   font=("Helvetica", 12),
                   width=10,
                   command=lambda: createGame(game_frame))
    # grid layout
    add_player.grid(row=0, column=0, pady=5, padx=5)
    entry_player.grid(row=0, column=1, pady=5, padx=5)
    label_list.grid(row=0, column=2, pady=5, padx=5)
    label_player_list.grid(row=0, column=3, pady=5, padx=5)
    button_size.grid(row=1, column=0, pady=5, padx=5)
    entry_size.grid(row=1, column=1, pady=5, padx=5)
    label_grid_size.grid(row=1, column=2, pady=5, padx=5)
    label_size.grid(row=1, column=3, pady=5, padx=5)
    start.grid(row=2, column=0, pady=5, padx=5)

    game_config.pack()


if __name__ == '__main__':
    root = Tk()

    gameConfig(root)
    root.mainloop()
