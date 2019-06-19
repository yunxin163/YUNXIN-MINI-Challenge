# @Author  :_kerman jt
# @Time    : 19-5-31 下午8:43


class MsgSyncMoveOperation(object):
    def __init__(self):
        self.__protoName = 'MsgSyncMoveOperation'
        self.__masterOrPlayer = None
        self.__id = None
        self.__horizontalFactory = None
        self.__verticalFactory = None
        self.__crouch = None
        self.__shift = None
        self.__jump = None

    def MsgRecive(self, msgDict):
        self.__enemyOrPlayer = msgDict['masterOrPlayer']
        self.__id = msgDict['id']
        self.__horizontalFactor = msgDict['horizontalFactor']
        self.__verticalFactor = msgDict['verticalFactor']
        self.__crouch = msgDict['crouch']
        self.__shift = msgDict['shift']
        self.__jump = msgDict['jump']

    def GetProtoName(self):
        return self.__protoName

    def GetId(self):
        return self.__id

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncMoveOperation'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['horizontalFactor'] = self.__horizontalFactor
        JsonStr['verticalFactor'] = self.__verticalFactor
        JsonStr['crouch'] = self.__crouch
        JsonStr['shift'] = self.__shift
        JsonStr['jump'] = self.__jump

        return JsonStr


class MsgSyncLocation(object):
    def __init__(self):
        self.__protoName = 'MsgSyncLocation'
        self.__masterOrPlayer = None
        self.__id = None
        self.__CurrClientId = None
        """ represents the rotation of curr player """
        self.__locationX = None
        self.__locationY = None
        self.__locationZ = None

    def GetMasterOrPlayer(self):
        return self.__masterOrPlayer

    def MsgRecive(self, msgDict):
        self.__masterOrPlayer = msgDict['masterOrPlayer']
        self.__id = msgDict['id']
        self.__CurrClientId = msgDict['CurrClientId']
        self.__locationX = msgDict['locationX']
        self.__locationY = msgDict['locationY']
        self.__locationZ = msgDict['locationZ']

    def GetProtoName(self):
        return self.__protoName

    def GetId(self):
        return self.__id

    def GetCurrClientId(self):
        return self.__CurrClientId

    def GetValue(self):
        return [self.__locationX, self.__locationY, self.__locationZ]

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncLocation'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['CurrClientId'] = self.__CurrClientId
        JsonStr['locationX'] = self.__locationX
        JsonStr['locationY'] = self.__locationY
        JsonStr['locationZ'] = self.__locationZ

        return JsonStr


class MsgSyncRotation(object):
    def __init__(self):
        self.__protoName = 'MsgSyncRotation'
        self.__masterOrPlayer = None
        self.__id = None
        self.__CurrClientId = None
        """ represents the rotation of curr player """
        self.__rotationX = None
        self.__rotationY = None
        self.__rotationZ = None

    def MsgRecive(self, msgDict):
        self.__masterOrPlayer = msgDict['masterOrPlayer']
        self.__id = msgDict['id']
        self.__CurrClientId = msgDict['CurrClientId']
        self.__rotationX = msgDict['rotationX']
        self.__rotationY = msgDict['rotationY']
        self.__rotationZ = msgDict['rotationZ']

    def GetEnemyOrPlayer(self):
        return self.__masterOrPlayer

    def GetProtoName(self):
        return self.__protoName

    def GetId(self):
        return self.__id

    def GetCurrClientId(self):
        return self.__CurrClientId

    def GetValue(self):
        return [self.__rotationX, self.__rotationY, self.__rotationZ]

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncRotation'
        JsonStr['enemyOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['CurrClientId'] = self.__CurrClientId
        JsonStr['rotationX'] = self.__rotationX
        JsonStr['rotationY'] = self.__rotationY
        JsonStr['rotationZ'] = self.__rotationZ

        return JsonStr


class MsgSyncAttack(object):
    def __init__(self):
        self.__protoName = 'MsgSyncAttack'
        self.__masterOrPlayer = None
        """ 用于表示攻击者是机器人还是其他玩家 """
        self.__AttackerMasterOrPlayer = None
        """ represents the id of attacker """
        self.__attacker = None
        """ represents the id of attacked """
        self.__attacked = None
        self.__hurt = None

    def MsgRecive(self, msgDict):
        self.__masterOrPlayer = msgDict['masterOrPlayer']
        self.__AttackerMasterOrPlayer = msgDict['AttackerMasterOrPlayer']
        self.__attacker = msgDict['attacker']
        self.__attacked = msgDict['attacked']
        self.__hurt = msgDict['hurt']

    def GetProtoName(self):
        return self.__protoName

    def GetMasterOrPlayer(self):
        return self.__masterOrPlayer

    def GetAttackerMasterOrPlayer(self):
        return self.__AttackerMasterOrPlayer

    """ who send the message """

    def GetId(self):
        return self.__attacker

    def GetCurrClientId(self):
        return self.__attacked

    """ who is attacked """

    def GetAttacked(self):
        return self.__attacked

    def GetAttacker(self):
        return self.__attacker

    """ here is the value of damage """

    def GetValue(self):
        return self.__hurt

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncAttack'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['AttackerMasterOrPlayer'] = self.__AttackerMasterOrPlayer
        JsonStr['attacker'] = self.__attacker
        JsonStr['attacked'] = self.__attacked
        JsonStr['hurt'] = self.__hurt

        return JsonStr


class MsgSyncReload(object):
    def __init__(self):
        self.__protoName = 'MsgSyncReload'
        self.__masterOrPlayer = None
        self.__id = None

    def MsgRecive(self, msgDict):
        self.__masterOrPlayer = msgDict['masterOrPlayer']
        self.__id = msgDict['id']

    def GetProtoName(self):
        return self.__protoName

    """ who send the message """

    def GetId(self):
        return self.__id

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncReload'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id

        return JsonStr


class MsgSyncUpgrade(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncUpgrade'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__grade = None

    def SetValue(self, grade):
        self.__grade = grade

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncUpgrade'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['grade'] = self.__grade


class MsgSyncWeapon(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncWeapon'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__weaponType = None

    def SetValue(self, weaponType):
        self.__weaponType = weaponType

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncWeapon'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['weaponType'] = self.__weaponType


class MsgSyncArmor(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncArmor'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__armorType = None

    def SetValue(self, armorType):
        self.__armorType = armorType

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncArmor'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['armorType'] = self.__armorType


class MsgSyncSpecial(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncSpecial'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__specialType = None

    def SetValue(self, specialType):
        self.__specialType = specialType

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncSpecial'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['specialType'] = self.__specialType


class MsgSyncLifeBar(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncLifeBar'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__lifeBar = None

    def SetValue(self, lifeBar):
        self.__lifeBar = lifeBar

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncLifeBar'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['lifeBar'] = self.__lifeBar


class MsgSyncEnergyBar(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncEnergyBar'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__lifeBar = None

    def SetValue(self, energyBar):
        self.__energyBar = energyBar

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncEnergyBar'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['energyBar'] = self.__energyBar


class MsgSyncNew(object):
    def __init__(self, masterOrPlayer, personId):
        self.__protoName = 'MsgSyncNew'
        self.__masterOrPlayer = masterOrPlayer
        self.__id = personId
        self.__grade = None
        self.__lifeBar = None
        self.__energyBar = None
        self.__weaponType = None
        self.__armorType = None
        self.__specialListType = None
        """ represents the location of curr player """
        self.__locationX = None
        self.__locationY = None
        self.__locationZ = None
        """ represents the rotation of curr player """
        self.__rotationX = None
        self.__rotationY = None
        self.__rotationZ = None

    def GetProtoName(self):
        return self.__protoName

    def SetValue(self, instance):
        self.__grade = instance.GetGrade()
        self.__lifeBar = instance.GetLifeBar()
        self.__energyBar = instance.GetEnergyBar()
        self.__weaponType = instance.GetWeaponType()
        self.__armorType = instance.GetArmorType()
        self.__specialListType = instance.GetSpecialType()
        """ represents the location of curr player """
        self.__locationX = instance.GetlocationX()
        self.__locationY = instance.GetlocationY()
        self.__locationZ = instance.GetlocationZ()
        """ represents the rotation of curr player """
        self.__rotationX = instance.GetRotationX()
        self.__rotationY = instance.GetRotationY()
        self.__rotationZ = instance.GetRotationZ()

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSyncNew'
        JsonStr['masterOrPlayer'] = self.__masterOrPlayer
        JsonStr['id'] = self.__id
        JsonStr['grade'] = self.__grade
        JsonStr['lifeBar'] = self.__lifeBar
        JsonStr['weaponType'] = self.__weaponType
        JsonStr['armorType'] = self.__armorType
        JsonStr['specialListType'] = self.__specialListType
        JsonStr['locationX'] = self.__locationX
        JsonStr['locationY'] = self.__locationY
        JsonStr['locationZ'] = self.__locationZ
        JsonStr['rotationX'] = self.__rotationX
        JsonStr['rotationY'] = self.__rotationY
        JsonStr['rotationZ'] = self.__rotationZ

        return JsonStr
