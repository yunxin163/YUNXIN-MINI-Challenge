# @Author  :_kerman jt
# @Time    : 19-5-31 下午9:16

class MsgLogin(object):

    def __init__(self):
        self.__protoName = 'MsgLogin'
        self.__id = None
        self.__pw = None
        self.__result = None
        self.__errMsg = ''

    def MsgRecive(self, msgDict):
        self.__id = msgDict['id']
        self.__pw = msgDict['pw']
        self.__result = msgDict['result']

    def GetProtoName(self):
        return self.__protoName

    def GetId(self):
        return self.__id

    def GetPwd(self):
        return self.__pw

    def SetResult(self, result):
        self.__result = result

    def SetErr(self, errMsg):
        self.__errMsg = errMsg

    def JsonSerialize(self):
        JsonStr = {}
        JsonStr['protoName'] = 'MsgLogin'
        JsonStr['id'] = self.__id
        JsonStr['pw'] = self.__pw
        JsonStr['result'] = self.__result
        JsonStr['errMsg'] = self.__errMsg

        return JsonStr
