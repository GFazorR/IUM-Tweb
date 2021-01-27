from Filetto import *


def play():
  turn = 0
  createGrid()
  print_grid()
  print('---------------------------')
  scoreboard = [0] * len(players)
  while True:
    curr_player = players[turn % len(players)]
    print("Player: ", curr_player, " turn")
    x = input("Insert x coord: ")
    y = input("Insert y coord: ")
    if x.isnumeric() and y.isnumeric() and addItem(curr_player, int(x), int(y)):
      scoreboard[turn % len(scoreboard)] = calcScore(curr_player, filetto_grid)
      print_grid()
      print('---------------------------')
      print("Scoreboard: ", list(zip(players, scoreboard)))
    else:
      turn -= 1
    if scoreboard[turn % len(scoreboard)] >= 50:
      print("Player ", curr_player, " Wins")
      break
    elif gameEnd(filetto_grid):
      print("Draw")
      break
    else:
      turn += 1


def main():
  global filetto_grid
  while True:
    value = input("Actions: p=play, q=quit: ")
    if value == 'p':
      players = ['X', 'Y']
      while True:
        value = input("Actions: s=start, g=grid size, p=insert players, q=quit: ")
        if value == 's':
          play()
        elif value == 'g':
          value = input("Set grid size: ")
          if value.isnumeric():
            setSize(int(value))
            print("Grid size: ", getSize())
        elif value == 'p':
          while True:
            value = input("Insert player, digit 'd' to stop: ")
            if (value == 'd'):
              break
            else:
              if not (value == '') and value not in players:
                players.append(value)
                print("Players: ", players)
              else:
                print("Invalid value")
        elif value == 'q':
          break
        else:
          print("Error invalid value")
    elif value == 'q':
      break
    else:
      print("Error invalid value")


if __name__ == "__main__":
  main()
