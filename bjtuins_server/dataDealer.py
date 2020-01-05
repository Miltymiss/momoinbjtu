import pymysql
conn = pymysql.connect(
    host='47.93.102.137',  # 我的IP地址
    port=3306,  # 不是字符串不需要加引号。
    user='root',
    password='androidtest',
    db='android_BJTUINS',
    charset='utf8'
)
cursor = conn.cursor()
#登陆
#入参：用户id，密码
#出参：User not exists 用户不存在
#     Login success 登陆成功
#     Password is wrong 密码错误
def login(uid,password):
    sql="select * from id_ifm where userid='%s'"%(uid)
    cursor.execute(sql)
    result = cursor.fetchall()
    if len(result)==0:
        return "User not exists"
    else:
        if result[0][1]==password:
            return "Login success"
        else:
            return "Password is wrong"

#注册
#入参：用户id，密码，用户名
#出参：exixts  用户已经存在
#     success 注册成功
def register(uid,password,uname):
    sql="call register('%s','%s','%s',%s)"%(uid,password,uname,'@result')
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()
    return result[0][0]

#获取好友信息列表
def getfriend(uid):
    sql="call get_friendlist('%s')"%(uid)
    cursor.execute(sql)
    result = cursor.fetchall()
    if len(result) == 0:
        print("false")
    else:
        return result

#更新用户信息
def updateusrifm(uid,username,birthday,sex,discirption,graph):
    if graph=='null':
        sql="update user_ifm set user_name='%s',birthday='%s',sex=%s,discription='%s' where user_id='%s' "%(username,birthday,sex,discirption,uid)
    else:
        sql="update user_ifm set user_name='%s',birthday='%s',sex=%s,discription='%s',graph='%s' where user_id='%s' "%(username,birthday,sex,discirption,graph,uid)
    cursor.execute(sql)
    result = cursor.fetchall()
    print(result)
    conn.commit()
#获取用户信息
def getuserifm(uid):
    sql="call get_userifm('%s')"%(uid)
    cursor.execute(sql)
    result=cursor.fetchall()
    return result
#添加日志
def addlog(uid,operation,time):
    sql="insert into log_list (user_id,operation,operation_time) values('%s','%s','%s')"%(uid,operation,time)
    cursor.execute(sql)
    conn.commit()

#添加聊天记录
def addchatlog(from_id,to_id,content,time):
    sql="insert into chat_log (chat_msg,from_id,time,to_id) values('%s','%s','%s','%s')"%(content,from_id,time,to_id)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()

#删除好友
def deletefriend(uid,fid):
    sql="delete from friend_list where user_id='%s' and friend_id='%s'"%(uid,fid)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()

#添加好友
def addfriend(uid,fid):
    sql="call add_friend('%s','%s',@result)"%(uid,fid)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()
    print(result[0][0])

#添加消息
def addmessage(uid,graph,text,video,time):
    sql="insert into message_list (user_id,graph,text,video,time) values ('%s','%s','%s','%s','%s')"%(uid,graph,text,video,time)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()

#删除消息
def deletemessage(mid):
    sql="delete from message_list where message_id=%s"%(mid)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()

#获取消息详情
#入参：消息id
#出参：
def getmessageifm(mid):
    sql="select * from message_list where message_id=%s"%(mid)
    cursor.execute(sql)
    result = cursor.fetchall()
    if len(result) == 0:
        return 'false'
    else:
        return result
        for item in result:
            print(item)
#添加评论
def addcomment(uid,mid,content,time):
    sql="insert into comment_list (message_id,user_id,comment,time) values ('%s',%s,'%s','%s')"%(mid,uid,content,time)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()
    print(result)
#获取消息的所有评论
def getcommentlist(mid):
    sql="call get_commentlist(%s)"%(mid)
    cursor.execute(sql)
    result=cursor.fetchall()
    print(result)
#删除评论
def deletecomment(cid):
    sql="call delete_comment('%s',@result) "%(cid)
    cursor.execute(sql)
    result=cursor.fetchall()
    print(result)
#获取好友动态
def getfriendmessage(uid):
    sql="call get_friendmessage('%s')"%(uid)
    cursor.execute(sql)
    result=cursor.fetchall()
    return result

def getfriendvmessage(uid):
    sql = "call get_friendvessage('%s')" % (uid)
    cursor.execute(sql)
    result = cursor.fetchall()
    return result

def addlike(uid,mid):
    sql="insert into like_list (user_id,message_id,likes) values('%s',%s,1)"%(uid,mid)
    cursor.execute(sql)
    result=cursor.fetchall()
    conn.commit()
    return result

def droplike(uid,mid):
    sql = "delete from like_list where user_id='%s',message_id='%s',likes=1" % (uid, mid)
    cursor.execute(sql)
    result = cursor.fetchall()
    conn.commit()
    return result

if __name__ == "__main__":
    getmessageifm(1)