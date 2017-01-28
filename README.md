# fd-tuiter11
Tuiter11 is a web application based on the famous social network Twitter. The application allows you to send plain text messages that are displayed in the user's home page. Users can follow other users, seeing himself in his timeline future publications of users followed. Tweets are public, except for users with private accounts, in which only their tweets will be if users follow each other. Users can also block other users so they can not see their posts. We can also save tweets as favorites for easy viewing in the desired time. Finally, the application allows retweet of a publication, thus appearing as tweet of the user who has retweeted, but always referring the user to the original publication.
Moreover, users can search for other users using keywords and view their profiles and edit information about your own profile, and also search tweets by keywords.

### Requirements

To run the project yo need the following software:

* Java 7 JDK
* maven
* mongo DB

### Donwload

To download the project:

	git clone https://github.com/iago-suarez/fd-tuiter11

### Run it!

Let's compile and deploy the project:

	cd fd-tuiter11
	mvn install
	cd tuiter11-webapp/
	mvn tomcat7:run

Now the webapp is running, yo can see the result in thw url: 

	http://localhost:8080/tuiter11-webapp/

### More Info

For more details see the [Spanish Project Report](https://github.com/iago-suarez/fd-tuiter11/blob/master/doc/Tuiter11-Informe_Proyecto.pdf?raw=true).