# @Author  :_kerman jt
# @Time    : 19-5-29 下午11:40

import logging
from redis import StrictRedis

"""
# In this project, I use redis to save user info and player info ,when a gaming start.
# Actually, user info and player info are in diffirent server.
# So, when the Gameserver start, there are two connections to redis,
# representing user-table and player-table respectively.
# Besides, AOF and RDF are applaied togther to persistent data
#### User-table: save the info of user, like username and password, grade,etc.
#### GamingPlayer-table: save the info of gamingplayer, like lifebar, energybar,curr_weapons, curr_armor,etc.
"""

global redisUserTable
global redisGamingPlayerTable


class RedisDataBaseMgr(object):

    def clearDatabase(self):
        redisUserTable.flushall()
        redisGamingPlayerTable.flushall()

    def RedisStart(self):
        try:
            global redisUserTable
            redisUserTable = StrictRedis(host='localhost', port=6379, db=0,
                                         password=None, decode_responses=True)
            redisUserTable.ping()
        except Exception as e:
            logging.exception(e)

        try:
            global redisGamingPlayerTable
            redisGamingPlayerTable = StrictRedis(host='localhost', port=6379, db=0,
                                                 password=None, decode_responses=True)
            redisGamingPlayerTable.ping()
        except Exception as e:
            logging.exception(e)

    def AddPlayer(self, playerId, playerDataDict):
        """ add player to redis """
        redisGamingPlayerTable.hmset('player:' + playerId, playerDataDict)

    def Login(self, username, passwd):
        if redisUserTable.exists('user:' + username) is 1:
            if redisUserTable.get('user:' + username) != passwd:
                errMsg = 'Password Error!'
                return errMsg

        else:
            errMsg = 'Id does not exist!'
            return errMsg

    def GetPlayerDataFromRedis(self, playerId):
        redisGamingPlayerTable.hset('player:' + playerId, 'ifOnline', 'True')
        return redisGamingPlayerTable.hgetall('player:' + playerId)

    def GetPlayerDataFromMysql(self, UserId):
        pass

    def SaveStateToRedis(self, linkPlayer):
        playerState = linkPlayer.GetSaveData()
        redisGamingPlayerTable.hmset('player:' + linkPlayer.GetPlayerId(), playerState)

    def SaveResultToMysql(self, linkPlayer):
        playerResult = linkPlayer.GetSaveData()
        #将经验值，金币等保存到用户表中