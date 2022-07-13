from distutils.spawn import spawn
from itertools import count
from opcode import opname
from random import randrange


from matplotlib.pyplot import xscale
from numpy import block
from graphics import *
import time

class Snake:

    plength = 50
    pwidth = 40
    b = None
    board = [[0 for x in range(20)] for y in range(20)]
    pellet = None
    direction = "none"
    Snake = []
    path = []
    hs = {}

    def __init__(self):
        choice = self.menu()
        self.readHs()

        if choice == "Play":
            ag = True
            first = True
            while ag:
                if not first:
                   self.b.close()
                   self.Snake = []
                   self.path = []
                self.b = GraphWin('Snake',1000,800)
                self.setBoard()
                self.b.setBackground('black')
                ag = self.play()
                self.b.getMouse()
                first = False
            self.b.close()
        elif choice == "AI":
            ag = True
            first = True
            while ag:
                if not first:
                   self.b.close()
                   self.Snake = []
                   self.path = []
                self.b = GraphWin('Snake',1000,800)
                self.setBoard()
                self.b.setBackground('black')
                ag = self.playAi()
                self.b.getMouse()
                first = False
            self.b.close()
        else:
            pass

    def readHs(self):
        f = open('Highscore','r')
        for l in f:
            print(l)


    def menu(self):
        def setting(ts,s):
            ts.setSize(s)
            ts.setFill("white")
            ts.setOutline("White")
            ts.draw(menu)

        menu = GraphWin('Snake',300,400)
        menu.setBackground('Black')
        ts = Text(Point(150,50),"Snake")
        setting(ts,36)
        tp = Text(Point(150,150),"Play")
        setting(tp,24)
        tai = Text(Point(150,250),"AI")
        setting(tai,24)
        ths = Text(Point(150,350),"High Score")
        setting(ths,24)

        t = ""
        while True:
            clicked = menu.getMouse()
            x = clicked.getX()
            y = clicked.getY()
            if(x > 105 and x < 195 and y > 125 and y < 175):
                t = "Play"
                break
            if(x > 120 and x < 180 and y > 225 and y < 275):
                t = "AI"
                break
            if(x > 60 and x < 240 and y > 325 and y < 375):
                t = "HS"
                break
        menu.close()
        return t
    
    def setBoard(self):
        for x in range(20):
            for y in range(20):
                rect = Rectangle(Point(x*self.plength,y*self.pwidth),Point((x*self.plength)+self.plength,(y*self.pwidth)+self.pwidth))
                rect.draw(self.b)
                rect.setFill('black')
                rect.setOutline('white')
        self.spawnPellet()
        self.createSnake()
        
    def createSnake(self):
        y = 10
        for x in range(10,6,-1):
            self.Snake.append(Rectangle(Point(x*self.plength,y*self.pwidth),Point((x*self.plength)+self.plength,(y*self.pwidth)+self.pwidth)))
            self.path.append("Right")
        for r in self.Snake:
            r.draw(self.b)
            r.setFill('white')
        
    def play(self):
        gameOver = False
        self.b.getKey()
        while not gameOver:
            self.direction = self.b.checkKey()
            self.move(self.direction)
            self.handlePellet()
            gameOver = self.gameOver()
            time.sleep(.08)
        again = Text(Point(500,380),"Do you want to play again")
        again.setFill("chartreuse1")
        again.setSize(36)
        again.draw(self.b)
        yes = Text(Point(400,420),"YES")
        yes.setFill("chartreuse1")
        yes.setSize(36)
        yes.draw(self.b)
        no = Text(Point(600,420),"NO")
        no.setFill("chartreuse1")
        no.setSize(36)
        no.draw(self.b)
        while True:
            clicked = self.b.getMouse()
            x = clicked.getX()
            y = clicked.getY()
            if(x > 350 and x < 450 and y > 400 and y < 440):
                return True
            if(x > 550 and x < 650 and y > 400 and y < 440):
                return False

    def playAi(self):
        gameOver = False
        self.b.getKey()
        while not gameOver:
            self.direction = self.getAiMove(3)
            print(self.direction)
            self.move(self.direction)
            self.handlePellet()
            gameOver = self.gameOver()
            time.sleep(.01)

    def getAiMove(self,max):
        print('========================================================================')
        xdis = (self.pellet.getP1().getX()/self.plength) - (self.Snake[0].getP1().getX()/self.plength)
        ydis = (self.pellet.getP1().getY()/self.pwidth) - (self.Snake[0].getP1().getY()/self.pwidth)


        for i in range(max,0,-1):
            print(i)
            print(self.snakeInWayDown(i))
            print(self.snakeInWayUp(i))
            print(self.snakeInWayLeft(i))
            print(self.snakeInWayRight(i))
            print("-------------------------------------------------------------------------------")
            if ydis > 0:
                if not self.snakeInWayDown(i):
                    return "Down"
                elif not self.snakeInWayUp(i):
                    return "Up"
                elif not self.snakeInWayLeft(i):
                    return "Left"
                elif not self.snakeInWayRight(i):
                    return "Right"
            if ydis < 0:
                if not self.snakeInWayUp(i):
                    return "Up"
                elif not self.snakeInWayDown(i):
                    return "Down"
                elif not self.snakeInWayLeft(i):
                    return "Left"
                elif not self.snakeInWayRight(i):
                    return "Right"
            if xdis < 0:
                if not self.snakeInWayLeft(i):
                    return "Left"
                elif not self.snakeInWayRight(i):
                    return "Right"
                elif not self.snakeInWayUp(i):
                    return "Up"
                elif not self.snakeInWayDown(i):
                    return "Down"
            if xdis > 0:
                if not self.snakeInWayRight(i):
                    return "Right"
                elif not self.snakeInWayLeft(i):
                    return "Left"
                elif not self.snakeInWayUp(i):
                    return "Up"
                elif not self.snakeInWayDown(i):
                    return "Down"
        count = 0 


        print("Random")
        while True and count < 50:
            count += 1
            n = randrange(0,4)
            if n == 0 and self.path[0] != "Down" and not self.snakeInWayDown(1):
                return "Up"
            elif n == 1 and self.path[0] != "Up" and not self.snakeInWayUp(1):
                return "Down"
            elif n == 2 and self.path[0] != "Right" and not self.snakeInWayRight(1):
                return "Left"
            elif n == 3 and self.path[0] != "Left" and not self.snakeInWayLeft(1):
                return "Right"
            elif self.snakeInWayLeft(1) and self.snakeInWayRight(1) and not self.snakeInWayUp(1) and not self.snakeInWayDown(1):
                return 'Down'
        
        return "Up"

    def snakeInWayLeft(self,buff):
        x = self.Snake[0].getP1().getX()
        y = self.Snake[0].getP1().getY()

        for i in range(1,len(self.Snake)):
            r = self.Snake[i]
            rx = r.getP1().getX()
            ry = r.getP1().getY()
            for i in range(buff):
                if (ry == y and rx + self.plength*(i+1) == x) or x == 0:
                    return True
        return False
        
    def snakeInWayRight(self,buff):
        x = self.Snake[0].getP1().getX()
        y = self.Snake[0].getP1().getY()
        for i in range(1,len(self.Snake)):
            r = self.Snake[i]
            rx = r.getP1().getX()
            ry = r.getP1().getY()
            for i in range(buff):
                if (ry == y and rx - self.plength*(i+1) == x) or x == 950:
                    return True
        return False

    def snakeInWayUp(self,buff):
        x = self.Snake[0].getP1().getX()
        y = self.Snake[0].getP1().getY()

        for i in range(1,len(self.Snake)):
            r = self.Snake[i]
            rx = r.getP1().getX()
            ry = r.getP1().getY()
            for i in range(buff):
                if (ry + self.pwidth * (i + 1) == y and rx == x) or y == 0:
                    return True
        return False

    def snakeInWayDown(self,buff):
        x = self.Snake[0].getP1().getX()
        y = self.Snake[0].getP1().getY()
        for i in range(1,len(self.Snake)):
            r = self.Snake[i]
            rx = r.getP1().getX()
            ry = r.getP1().getY()
            for i in range(buff):
                if (ry - self.pwidth * (i+1) == y and rx == x) or y >= 750:
                    return True
        return False

    def move(self,ndir):
        temp = self.path[:]
        if ndir != "":
            self.path[0] = ndir
        for i in range(1,len(self.path)):
            self.path[i] = temp[i-1]
        count = 0
        for r in self.Snake:
            cdir = self.path[count]
            if cdir == "Right":
                r.move(self.plength,0)
            elif cdir == "Left":
                r.move(-self.plength,0)
            elif cdir == "Up":
                r.move(0,-self.pwidth)
            elif cdir == "Down":
                r.move(0,self.pwidth)
            count+=1

    def gameOver(self):
        headp1x = self.Snake[0].getP1().getX()
        headp1y = self.Snake[0].getP1().getY()

        headp2x = self.Snake[0].getP2().getX()
        headp2y = self.Snake[0].getP2().getY()

        if headp1x < 0 or headp1y < 0 or headp2y > 800 or headp2x > 1000:
            return True
        if self.path[0] == "Right" and self.path[1] == "Left":
            return True
        if self.path[0] == "Up" and self.path[1] == "Down":
            return True
        if self.path[0] == "Left" and self.path[1] == "Right":
            return True
        if self.path[0] == "Down" and self.path[1] == "UP":
            return True

        for i in range(1,len(self.Snake)):
            curp1x = self.Snake[i].getP1().getX()
            curp1y = self.Snake[i].getP1().getY()
            curp2x = self.Snake[i].getP2().getX()
            curp2y = self.Snake[i].getP2().getY()     
            if curp1x == headp1x and curp1y == headp1y and curp2x == headp2x and headp2y == curp2y:
                return True
        
        return False

    def spawnPellet(self):

        while True:
            x = randrange(20) * self.plength
            y = randrange(20) * self.pwidth
            good = True
            for r in self.Snake:
                xs = r.getP1().getX()
                ys = r.getP1().getY()
                if(x == xs and y == ys):
                    good = False
            if(good):
                break

        self.pellet = Rectangle(Point(x,y),Point(x+self.plength,y+self.pwidth))
        self.pellet.draw(self.b)
        self.pellet.setFill("Yellow")

    def handlePellet(self):
        headp1x = self.Snake[0].getP1().getX()
        headp1y = self.Snake[0].getP1().getY()
        if headp1x == self.pellet.getP1().getX() and headp1y == self.pellet.getP1().getY():
            self.pellet.undraw()
            self.increaseSnake()
            self.spawnPellet()

    def increaseSnake(self):
        cdir = self.path[-1]

        headp1x = self.Snake[-1].getP1().getX()
        headp1y = self.Snake[-1].getP1().getY()
        headp2x = self.Snake[-1].getP2().getX()
        headp2y = self.Snake[-1].getP2().getY()

        if cdir == "Right":
            nRec = Rectangle(Point(headp1x-self.plength,headp1y),Point(headp2x-self.plength,headp2y))
            nRec.draw(self.b)
            nRec.setFill("White")
            self.Snake.append(nRec)
            self.path.append("Right")
        elif cdir == "Left":
            nRec = Rectangle(Point(headp1x + self.plength,headp1y),Point(headp2x+self.plength,headp2y))
            nRec.draw(self.b)
            nRec.setFill("White")
            self.Snake.append(nRec)
            self.path.append("Left")
        elif cdir == "Up":
            nRec = Rectangle(Point(headp1x,headp1y+self.pwidth),Point(headp2x,headp2y+self.pwidth))
            nRec.draw(self.b)
            nRec.setFill("White")
            self.Snake.append(nRec)
            self.path.append("Up")
        elif cdir == "":
            nRec = Rectangle(Point(headp1x,headp1y-self.pwidth),Point(headp2x,headp2y-self.pwidth))
            nRec.draw(self.b)
            nRec.setFill("White")
            self.Snake.append(nRec)
            self.path.append("Down")
                
if __name__ == '__main__':
    snakeGame = Snake()