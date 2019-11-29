#Android Project:INS-BJTU
##1 Background  
&emsp;&emsp;&emsp;&emsp;Because of firewall on the mainland of China,most of people can't use Instgram.At the same time,there's fiew app as same as ins in mainland China.In order to provide an application which has most of the functions,we decided to develop this android application.
##2 Basic Functions 
###2.1 Register&Log in  
&emsp;&emsp;&emsp;&emsp;Every user who uses our appilication must have an account number.Before using the main funtions of the application, user must login with their account.The login function will send the account number and password that user input to server and the server will check the account in the database.If the account is exist in the DB and password is correct，the application will turn to zhe mainframe.
***
###2.2 Main Frame
####2.2.1 View Informations  
&emsp;&emsp;&emsp;&emsp;When after logging in or double click home button,the frame will refresh the imformations that published by users.User can slide the screen up and down to browse.The information will display in card form.
####2.2.2 Comment
&emsp;&emsp;&emsp;&emsp;It allows user to leave comment under the information that other user published.The comment must less than 128 chars.
####2.2.3 Like
&emsp;&emsp;&emsp;&emsp;It allows user to show like to the information,the like will be counted and users who see this information will know who click like and how many people click like.User who publish this information will be remind when someone click 'like'.You can click 'Like' twice to canlce the like you show.
####2.2.4 Dislike
&emsp;&emsp;&emsp;&emsp;It allows user to show dislike to the information.The number of 'dislike' will be counted.User who publish this imformation won't know who click"dislike",but the total number of dislike will be shown on the card.You can click 'Like' twice to canlce the dislike you show.  
####2.2.5 Search
&emsp;&emsp;&emsp;&emsp;It allows user search the informations or users.

---
###2.3 View user information
####2.3.1 User information
&emsp;&emsp;&emsp;&emsp;It shows the basic information of selected user,Includes:User's nickname,introduce,date of birth and so on.
####2.3.2 Follow
&emsp;&emsp;&emsp;&emsp;You can click follow inorder to let the information of the user push to your screen.

---
###2.4 Private chat
####2.4.1 Chat
&emsp;&emsp;&emsp;&emsp;It allows user has a private chat with another user.
####2.4.2 Join the blacklist
&emsp;&emsp;&emsp;&emsp;Join the selected uestr to the blacklist after this,the user can't leave comment on the information you published and also can't send private chat message to you.
####2.4.3	 Not recieve message
&emsp;&emsp;&emsp;&emsp;When choose this you will refuse to recieve private chat message that the selected user send to you.