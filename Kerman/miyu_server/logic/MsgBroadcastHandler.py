# @Author  :_kerman jt
# @Time    : 19-5-29 下午11:35

import json


class MsgBroadcastHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        """ for all other clients online """
        self._broadCast(msgObj, currCliConn)

    """ broadcast the sync message to all other client """

    def _broadCast(self, msgObj, currCliConn):
        """ for all the clients """
        for personId in currCliConn.keys():
            """ msg need to be broadcasted """
            msg = json.dumps(msgObj.JsonSerialize())
            """ put to write buffer """
            currCliConn[personId].GetWriteBuffer().put(msg)
