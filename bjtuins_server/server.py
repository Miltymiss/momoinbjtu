import json
import sys
import os
from urllib import request
import dataDealer
from flask_sockets import Sockets
import time
from gevent import monkey
from flask import *
from gevent import pywsgi
import datetime

'''
import multiprocessing
import uuid
from threadpool import ThreadPool, makeRequests
'''
sys.path.append(os.path.abspath(os.path.dirname(__file__) + '/' + '..'))
sys.path.append("..")
monkey.patch_all()
app = Flask(__name__)
sockets = Sockets(app)
now = time.strftime('%Y-%m-%d-%H-%M-%S', time.localtime(time.time()))

#与web进行通信
@app.route('/')

@app.route('/register',methods=["POST","GET"])
def register():
    uid = request.args['userid']
    password = request.args['password']
    uname=request.args['username']
    print('register operation userid:%s,password:%s,username:%s' % (uid, password,uname))
    response=dataDealer.register(uid, password, uname)
    print('response:'+response)
    if response=='exists':
        return {"status":False,"response":"userid exists"}
    else:
        return {"status":True,"response:":"Register success"}

@app.route('/login',methods=["POST","GET"])
def login():
    uid = request.args['userid']
    password = request.args['password']
    print('login operation userid:%s,password:%s'%(uid,password))
    response=dataDealer.login(uid,password)
    print('response:'+response)
    if response=='User not exists':
        return json.dumps({"status":False,"response:":"User not exists"})
    elif response=='Password is wrong':
        return json.dumps({"status":False,"response:":"Pass word is wrong"})
    elif response=='Login success':
        return json.dumps({"status":True,"response:":"Login success"})
    else:
        return json.dumps({"status":False,"response:":"Login error"})

@app.route("/addfriend",methods=["POST","GET"])
def addfriend():
    reqifm = request.get_json()
    uid = reqifm.args["uid"]
    fid = reqifm.args["fid"]
    response = dataDealer.addfriend(uid, fid)
    if response == "exists":
        return {"status": False, "response": "Friend already exists"}
    else:
        return {"status": True, "response": "Add success"}

@app.route("/deletefriend", methods=["POST","GET"])
def deletefriend():
    """删除好友"""
    # 获取参数：用户id，好友id
    # 已有参数
    reqifm = request.get_json()
    uid = reqifm.get("uid")
    fid = reqifm.get("fid")
    dataDealer.deletefriend(uid, fid)
    return {"status": True, "response": "Add success"}

@app.route("/getfriendlist",methods=["POST","GET"])
def getfriendlist():
    uid = request.args['userid']
    result = dataDealer.getfriend(uid)
    return {"status": True, "response": result}

@app.route("/updateuserifm",methods=["POST","GET"])
def updateuserifm():
    uid=request.args["userid"]
    print(uid)
    uname=request.args["username"]
    print(uname)
    birthday=request.args["birthday"]
    print(birthday)
    sex=request.args["sex"]
    print(sex)
    discription=request.args["discription"]
    if request.args["graph"]=='not':
        graph='null'
    else:
        graph=request.args["graph"]
    dataDealer.updateusrifm(uid,uname,birthday,sex,discription,graph)

    return {"status": True, "response": "Update success"}

@app.route("/addmessage",methods=["POST","GET"])
def addmessage():
    uid=request.args["userid"]
    content=request.args["text"]
    type=request.args["media"]
    dir='/Users/zhangjiahua/Desktop/BJTU-INS/server/media/'+uid
    if type=='graph':
        img=request.files.get("img")
        if not os.path.exists(dir+'/img'):
            os.makedirs(dir+'/img')
        filenum = len(os.listdir(dir+'/img/'))
        file = os.path.splitext(img.name)
        filename,type=file
        imgurl = dir + '/img/'+str(filenum)+type
        img.save(imgurl)
        videourl='null'
    elif type.get("media")=='video':
        video=request.files.get("imgs")
        if len(video)>0:
            if not os.path.exists(dir+'/video'):
                os.makedirs(dir+'/video')
            filenum = len(os.listdir(dir+'/img/'))
            file = os.path.splitext(video.name)
            filename, type = file
            videourl = dir + '/video/'+filenum+type
            video.save(videourl)
            imgurl='null'
    now_time = datetime.datetime.now()
    time=datetime.datetime.strftime(now_time,'%Y-%m-%d %H:%M:%S')
    dataDealer.addmessage(uid,imgurl,content,videourl,time)
    return {"status": True, "response": "Add success"}

@app.route("/getmessageifm",methods=["GET","POST"])
def getmessageifm():
    reqifm = json.loads(request.get_data(as_text=True))
    mid=reqifm.get("message_id")
    result=dataDealer.getmessageifm(mid)
    return {"status": True, "response": result}

@app.route("/show/<string:filename>",methods=["GET","POST"])
def show_img(filename):
    image_data = open(filename, "rb").read()
    response = make_response(image_data)
    response.headers['Content-Type'] = 'image/png'
    return response

@app.route("/comment",methods=["GET","POST"])
def leavecomment():
    uid=request.args["userid"]
    mid=request.args["messageid"]
    content=request.args["content"]
    now_time = datetime.datetime.now()
    time = datetime.datetime.strftime(now_time, '%Y-%m-%d %H:%M:%S')
    dataDealer.addcontent(uid,mid,content,time)
    return {"status": True, "response": "Comment Success"}

@app.route("/getcomment",methods=["POST","GET"])
def getcomment():
    mid=request.args["messageid"]
    result=dataDealer.getcommentlist(mid)
    rr=[]
    ss=[]
    for items in result:
        i=0
        for item in items:
            if i==5:
                ss.append(datetime.datetime.strftime(item[5], '%Y-%m-%d %H:%M:%S'))
            else:
                ss.append(item)
        rr.append(ss)
    return {"status": True, "response": rr}

@app.route("/getfriendmessage",methods=["GET","POST"])
def getfriendmessage():

    uid=request.args["userid"]
    ss = []
    rr=[]
    result=dataDealer.getfriendmessage(uid)
    for items in result:
        i=0
        for item in items:
            if i==9:
                break
            ss.append(item)
            i=i+1
        ss.append(datetime.datetime.strftime(items[9], '%Y-%m-%d %H:%M:%S'))
        rr.append(ss)
        ss=[]

    return {"status": True, "response": rr}

@app.route("/getfriendvmessage",methods=["GET","POST"])
def getfriendvmessage():
    uid = request.args["userid"]
    ss = []
    rr = []
    result = dataDealer.getfriendvmessage(uid)
    for items in result:
        i = 0
        for item in items:
            if i == 9:
                break
            ss.append(item)
            i = i + 1
        ss.append(datetime.datetime.strftime(items[9], '%Y-%m-%d %H:%M:%S'))
        rr.append(ss)
        ss = []
    return {"status": True, "response": rr}

@app.route("/getuserifm",methods=["GET","POST"])
def getuserifm():
    uid=request.args["userid"]
    result=dataDealer.getuserifm(uid)
    i=0
    ss=[]
    rr=[]

    for item in result:
        for items in item:
            if i==2:
                ss.append(datetime.datetime.strftime(items, '%Y-%m-%d'))
            else:
                ss.append(items)
            i=i+1
        if len(item)==0:
            break
    rr.append(ss)
    return {"status": True, "response": rr}

@app.route("/addlike",methods=["GET","POST"])
def addlike():
    uid=request.args["userid"]
    mid=request.args["messageid"]
    dataDealer.addlike(uid,mid)
    return {"status": True, "response": "Add like success"}

@app.route("/droplike",methods=["GET","POST"])
def droplike():
    uid=request.args["userid"]
    mid=request.args["messageid"]
    dataDealer.droplike(uid,mid)
    return {"status": True, "response": "Add like success"}

if __name__ == "__main__":
    server = pywsgi.WSGIServer(('0.0.0.0', 5050), app,)
    print('server start')
    server.serve_forever()