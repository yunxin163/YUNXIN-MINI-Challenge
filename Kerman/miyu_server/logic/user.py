# @Author  :_kerman jt
# @Time    : 19-5-29 下午4:14

class User(object):

    def __init__(self):
        self.__id = None
        self.__username = None
        self.__grade = None
        self.__ifOnline = 'Flase'
        self.__money = None

    def SetID(self, UserId: int):
        self.__id = UserId

    def GetId(self) -> int:
        return self.__id

    def SetUsername(self, UserName: str):
        self.__username = UserName

    def GetUsername(self) -> str:
        return self.__username

    def Getgrade(self) -> int:
        return self.__grade

    def GetifOnline(self) -> str:
        return self.__ifOnline
