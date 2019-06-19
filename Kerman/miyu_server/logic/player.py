# @Author  :_kerman jt
# @Time    : 19-5-29 下午3:21
import random

from logic.user import User
from logic.puzzle import Clues


class GamingPlayerBase(User):

    def __init__(self):
        User.__init__(self)
        self.__key = None
        self.__clues = {}
        self.__lifeBar = 100
        self.__energyBar = 100
        self.__weaponType = None
        self.__armorType = None
        self.__specialListType = []
        self.__iswin = None
        """ represents the location of curr player """
        self.__locationX = -300
        self.__locationY = 0
        self.__locationZ = 20
        """ represents the rotation of curr player """
        self.__rotationX = None
        self.__rotationY = None
        self.__rotationZ = None

        self.__maxspecial = 5

    def SetId(self, PlayerId: str):
        self.__id = PlayerId

    def SetIsWin(self, is_win: bool):
        self.__iswin = is_win

    def GetKey(self) -> int:
        return self.__key

    def GetClues(self) -> dict:
        return self.__clues

    def GetGrade(self) -> int:
        return self.__grade

    def GetLifeBar(self) -> int:
        return self.__lifeBar

    def GetEnergyBar(self) -> int:
        return self.__energyBar

    def GetWeaponType(self) -> dict:
        return self.__weaponType

    def GetArmorType(self) -> dict:
        return self.__armorType

    def GetSpecialType(self) -> list:
        return self.__specialListType

    def GetlocationX(self) -> float:
        return self.__locationX

    def GetlocationY(self) -> float:
        return self.__locationY

    def GetlocationZ(self) -> float:
        return self.__locationZ

    def GetRotationX(self) -> float:
        return self.__rotationX

    def GetRotationY(self) -> float:
        return self.__rotationY

    def GetRotationZ(self) -> float:
        return self.__rotationZ

    def GetPlayerId(self) -> str:
        return self.__id

    def GetIsWin(self) -> bool:
        return self.__iswin

    """ register call for Player """

    def GetInitDataDict(self) -> dict:
        playerDataDict = {}
        # playerDataDict['grade'] = #get from database
        playerDataDict['key'] = 1
        playerDataDict['clues'] = Clues()  # random clues
        playerDataDict['lifeBar'] = 100
        playerDataDict['energyBar'] = 100
        playerDataDict['weaponType'] = 0  # default weapon
        playerDataDict['armor'] = None
        playerDataDict['special'] = [].append(random.randint(1, 11))

        return playerDataDict

    """ register call for Player """
    """ when a play end, server need to save the data to database """

    def GetSavedData(self) -> dict:
        playerDataDict = {}
        if self.__iswin:
            playerDataDict['experience_value'] = random.randint(200, 300)
            playerDataDict['money'] = random.randint(25, 35)
        else:
            playerDataDict['experience_value'] = random.randint(50, 100)
            playerDataDict['money'] = random.randint(5, 15)

        return playerDataDict

    """ login call for Player """
    """ when user login, i need to init player data with the data from database """

    def SetPlayerData(self, dataFromRedis):
        self.__ifOnline = 'True'
        self.__grade = int(dataFromRedis['grade'])
        self.__username = str(dataFromRedis['username'])

    """ 
    # Update the location info
    # 只有客户端的玩家位置更新之后才会执行set函数
    """

    def SetLocation(self, location: list):
        self.__preLocationX = self.__locationX
        self.__preLocationY = self.__locationY
        self.__preLocationZ = self.__locationZ
        self.__locationX = location[0]
        self.__locationY = location[1]
        self.__locationZ = location[2]

    def SetRotation(self, rotation: list):
        self.__rotationX = rotation[0]
        self.__rotationY = rotation[1]
        self.__rotationZ = rotation[2]

    def AddSpecial(self, specialType: int):
        if len(self.__specialListType) < self.__maxspecial:
            self.__specialListType.append(specialType)
        else:
            errMsg = '道具已满'
            yield errMsg

    def ChangeWeapon(self, weaponType):
        self.__weaponType = weaponType

    def ChangeArmor(self, armorType):
        self.__armor = armorType

    def UpdateLifeBar(self, damage):
        self.__lifeBar = self.__lifeBar - damage

    def UpdateEnergyBar(self, spend):
        self.__energyBar = self.__energyBar - spend

    def SetLifeBar(self):
        self.__lifeBar = 100

    def SetEnergyBar(self):
        self.__energyBar = 100


class GamingPlayer(GamingPlayerBase):

    def __init__(self):
        GamingPlayerBase.__init__(self)
        self.__ifFighted = False
        self.__FightedId = None
        self.__ifDecryption = False
        self.__DecryptionId = None

    def AddFighted(self, FightedId: int):
        self.__FightedId = FightedId

    def AddDecryption(self, DecryptionId: int):
        self.__DecryptionId = DecryptionId

    def GetFighted(self) -> int:
        return self.__FightedId

    def GetDecryption(self) -> int:
        return self.__DecryptionId

    def SetIfFighted(self, flag: bool):
        self.__ifFighted = flag

    def SetIfDecryption(self, flag: bool):
        self.__ifDecryption = flag
