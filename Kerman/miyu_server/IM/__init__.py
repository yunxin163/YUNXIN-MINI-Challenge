# @Author  :_kerman jt
# @Time    : 2019/6/16 20:00
import ctypes
import os

dirpath = os.path.dirname(__file__)
# print(dirpath)
dllpath = dirpath+'/nim_chatroom.dll'
# print(dllpath)
dll = ctypes.windll.LoadLibrary(dllpath)
# Objdll = ctypes.WinDLL(dllpath)
print(dll)