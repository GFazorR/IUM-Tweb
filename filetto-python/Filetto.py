from itertools import groupby

global filetto_size, filetto_grid
players = ['X', 'Y']
filetto_size = 5
filetto_grid = []
scoreboard = []


def setSize(size):
  global filetto_size
  filetto_size = size


def getSize():
  return filetto_size


def gameEnd(grid):
  if not list(filter(lambda i: i == '', [item for s in grid for item in s])):
    return True
  else:
    return False


def createGrid():
  filetto_grid.clear()
  for i in range(filetto_size):
    filetto_row = []
    for j in range(filetto_size):
      filetto_row.append('')
    filetto_grid.append(filetto_row)


def print_grid():
  for i in range(filetto_size):
    print(filetto_grid[i])


def addItem(item, x, y):
  if x < filetto_size and y < filetto_size:
    if '' == filetto_grid[x][y]:
      filetto_grid[x][y] = item
      print_grid()
      print('---------------------------')
      return True
    else:
      print("Cell already full")
  else:
    print("Out of range")
  return False


# calcola il punteggio
def calcScore(symbol, grid):
  score = 0
  score += checkRow(symbol, grid)
  score += checkCol(symbol, grid)
  score += checkBackwardsDiagonals(symbol, grid)
  score += checkForwardDiagonals(symbol, grid)
  return score


def scoreDef(seq):
  if seq <= 2:
    return 0
  elif seq == 3:
    return 2
  elif seq == 4:
    return 10
  elif seq == 5:
    return 50


def checkRow(symbol, grid):
  score = 0
  for row in grid:
    score += sum(map(scoreDef,
                     [sum(1 for i in g)
                      for k, g in groupby(row)
                      if k == symbol]))
  return score


def checkCol(symbol, grid):
  rotated_list = [list(x) for x in zip(*grid)]
  return checkRow(symbol, rotated_list)


def checkBackwardsDiagonals(symbol, grid):
  b = [None] * (len(grid) - 1)
  grid1 = [b[i:] + r + b[:i] for i, r in enumerate(grid)]
  return checkRow(symbol, [[c for c in r if c is not None] for r in zip(*grid1)])


def checkForwardDiagonals(symbol, grid):
  b = [None] * (len(grid) - 1)
  grid1 = [b[:i] + r + b[i:] for i, r in enumerate(grid)]
  return checkRow(symbol, [[c for c in r if c is not None] for r in zip(*grid1)])



