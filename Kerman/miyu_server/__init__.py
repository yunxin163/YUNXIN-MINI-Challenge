# @Author  :_kerman jt
# @Time    : 19-6-2 下午1:43

from net.server import ServerNet
from database.redisdatabase import RedisDataBaseMgr


def main():
    """ Start Redis database """
    DataMgrIns = RedisDataBaseMgr()
    DataMgrIns.RedisStart()

    # DataMgrIns.ClearDatabase()

    """ start server """
    ServerNetInit = ServerNet()
    ServerNetInit.StartServer()

if __name__ == '__main__':
    main()