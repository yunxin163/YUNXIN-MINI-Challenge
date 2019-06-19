# @Author  :_kerman jt
# @Time    : 19-6-2 上午10:45

class MsgDropPropBase(object):
    def __init__(self):
        self.__key = None
        self.__locationX = None
        self.__locationZ = None

    def SetKey(self, key):
        self.__key = key

    def SetLoc(self, location):
        self.__locationX = location[0]
        self.__locationZ = location[1]

    def JsonSerializeBase(self):
        JsonStr = {}
        JsonStr['key'] = self.__key
        JsonStr['locationX'] = self.__locationX
        JsonStr['locationZ'] = self.__locationZ

        return JsonStr


class MsgDropWeapon(MsgDropPropBase):
    def __init__(self):
        super().__init__()
        self.__protoName = 'MsgDropWeapon'
        self.__type = None

    def SetType(self, weaponType):
        self.__type = weaponType

    def GetProtoName(self):
        return self.__protoName

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = self.__protoName
        JsonStr['type'] = self.__type
        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


class MsgDropArmor(MsgDropPropBase):
    def __init__(self):
        super().__init__()
        self.__protoName = 'MsgDropArmor'
        self.__type = None

    def SetType(self, armorType):
        self.__type = armorType

    def GetProtoName(self):
        return self.__protoName

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = self.__protoName
        JsonStr['type'] = self.__type
        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


class MsgDropSpecial(MsgDropPropBase):
    def __init__(self):
        super().__init__()
        self.__protoName = 'MsgDropSpecial'
        self.__type = None

    def SetType(self, armorType):
        self.__type = armorType

    def GetProtoName(self):
        return self.__protoName

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = self.__protoName
        JsonStr['type'] = self.__type
        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


"""*************************************************************************"""


class PropPickUpBase(object):
    def __init__(self):
        self.__id = None
        self.__key = None
        self.__result = None

    def MsgReciveBase(self, msgDict):
        self.__id = msgDict['id']
        self.__key = msgDict['key']
        self.__result = msgDict['result']

    def GetId(self):
        return self.__id

    def GetKey(self):
        return self.__key

    def SetErr(self):
        self.__result = 1

    def JsonSerializeBase(self):
        JsonStr = {}
        JsonStr['id'] = self.__id
        JsonStr['key'] = self.__key
        JsonStr['result'] = self.__result

        return JsonStr


class WeaponPickUp(PropPickUpBase):
    def __init__(self):
        super().__init__()
        self.__protoName = 'WeaponPickUp'
        self.__type = None

    def MsgRecive(self, msgDict):
        self.MsgReciveBase(msgDict)
        self.__type = msgDict['type']

    def GetProtoName(self):
        return self.__protoName

    def GetType(self):
        return self.__type

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'WeaponPickup'

        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


class ArmorPickUp(PropPickUpBase):
    def __init__(self):
        super().__init__()
        self.__protoName = 'ArmorPickUp'
        self.__type = None

    def MsgRecive(self, msgDict):
        self.MsgReciveBase(msgDict)
        self.__type = msgDict['type']

    def GetProtoName(self):
        return self.__protoName

    def GetType(self):
        return self.__type

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'ArmorPickUp'

        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


class SpecialPickUp(PropPickUpBase):
    def __init__(self):
        super().__init__()
        self.__protoName = 'SpecialPickUp'
        self.__type = None

    def MsgRecive(self, msgDict):
        self.MsgReciveBase(msgDict)
        self.__type = msgDict['type']

    def GetProtoName(self):
        return self.__protoName

    def GetType(self):
        return self.__type

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'SpecialPickUp'

        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


""" ************************************************************************** """


class MsgPropPickFinish(object):
    def __init__(self):
        self.__key = None
        self.__locationX = None
        self.__locationZ = None

    def MsgReciveBase(self, msgDict):
        self.__key = msgDict['key']
        self.__locationX = msgDict['locationX']
        self.__locationZ = msgDict['locationZ']

    def GetLoc(self):
        return [self.__locationX, self.__locationZ]

    def GetKey(self):
        return self.__key

    def JsonSerializeBase(self):
        JsonStr = {}
        JsonStr['key'] = self.__key
        JsonStr['locationX'] = self.__locationX
        JsonStr['locationZ'] = self.__locationZ

        return JsonStr


class MsgWeaponPickFinish(MsgPropPickFinish):
    def __init__(self):
        super().__init__()
        self.__protoName = 'MsgWeaponPickFinish'
        self.__type = None

    def MsgRecive(self, msgDict):
        self.MsgReciveBase(msgDict)
        self.__type = msgDict['type']

    def GetType(self):
        return self.__type

    def GetProtoName(self):
        return self.__protoName

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgWeaponPickFinish'
        JsonStr['type'] = self.__type

        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


class MsgArmorPickFinish(MsgPropPickFinish):
    def __init__(self):
        super().__init__()
        self.__protoName = 'MsgArmorPickFinish'
        self.__type = None

    def MsgRecive(self, msgDict):
        self.MsgReciveBase(msgDict)
        self.__type = msgDict['type']

    def GetType(self):
        return self.__type

    def GetProtoName(self):
        return self.__protoName

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgArmorPickFinish'
        JsonStr['type'] = self.__type

        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr


class MsgSpecialPickFinish(MsgPropPickFinish):
    def __init__(self):
        super().__init__()
        self.__protoName = 'MsgSpecialPickFinish'
        self.__type = None

    def MsgRecive(self, msgDict):
        self.MsgReciveBase(msgDict)
        self.__type = msgDict['type']

    def GetType(self):
        return self.__type

    def GetProtoName(self):
        return self.__protoName

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgSpecialPickFinish'
        JsonStr['type'] = self.__type

        JsonStrBase = self.JsonSerializeBase()
        for key in JsonStrBase:
            JsonStr[key] = JsonStrBase[key]

        return JsonStr
