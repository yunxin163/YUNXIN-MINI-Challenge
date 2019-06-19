# @Author  :_kerman jt
# @Time    : 19-5-29 下午3:03

class PropBase(object):

    def __init__(self):
        self.__key = None
        self.__locationX = None
        # self.__locationY = None
        self.__locationZ = None

    def SetLoc(self, location: list):
        self.__locationX = location[0]
        # self.__locationY = location[1]
        self.__locationZ = location[1]

    def GetLoc(self) -> list:
        return [self.__locationX, self.__locationZ]

    def SetKey(self, keyValue: int):
        self.__key = keyValue

    def GetKey(self) -> int:
        return self.__key


class Weapon(PropBase):

    def __init__(self):
        PropBase.__init__(self)
        self.__type = None

    def SetType(self, weaponType: int = 0):
        self.__type = weaponType

    def GetType(self) -> int:
        return self.__type


class Armor(PropBase):
    def __init__(self):
        PropBase.__init__(self)
        self.__type = None

    def SetType(self, armorType: int = 0):
        self.__type = armorType

    def GetType(self) -> int:
        return self.__type


class Special(PropBase):
    def __init__(self):
        PropBase.__init__(self)
        self.__type = None

    def SetType(self, specialType: int = 0):
        self.__type = specialType

    def GetType(self) -> int:
        return self.__type
