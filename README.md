**Presentation**

L@ME is a free software for processing and analyzing electronic messages, developed in JAVA under Eclipse.

It allows you to:

* Open locally (directories of) electronic messages (formats: EML, EMLX, MBX, MBX, MBOX, MBOXRD) or download messages located on a PhpBB forum or import forums / topics messages from an [Extractify json file](https://github.com/fredericvergnaud/extractify).
* View and obtain data for each message, author and discussion.
* Group discussions according to several parameters: the characters of the subject using Levenshtein's distance, the number of days between two similar subjects or the number of messages between two similar subjects.
* Clean up authors and topics of messages.
* Split and merge lists according to different parameters.
* Export messages in CSV format according to different parameters.
* Obtain general statistics on the complete list or certain periods of the list: follow-up date, number of speakers during the follow-up period, number of speakers having sent a single message, number of messages during the follow-up period, number of subjects during the follow-up period, average number of messages per speaker and per month, average number of different speakers per subject, average number of messages per subject.
* Obtain statistics on the dominant speakers of the complete list or certain periods of the list (based on a modifiable multiplier parameter of the participation intensity that sets a threshold above which a speaker is said to be "dominant"): who is the dominant speaker and how many messages has he sent (and percentage of the complete list), number of dominant speakers (and percentage of the complete list), number of small speakers (and percentage of the complete list), number of messages sent by the dominant speakers (and percentage of the complete list).
* Obtain statistics on the collective subjects of the complete list or of certain periods of the list (according to a modifiable multiplier parameter of the average number of messages per conversation - and if necessary the average number of speakers per conversation - which sets a threshold above which a conversation is called "collective subject"): number of collective topics (and percentage of total number of topics), number of messages in the collective topics (and percentage of total number of messages), percentage of messages from the dominant speaker in the collective topics, percentage of messages from the dominant speakers in the collective topics, percentage of messages from the small speakers in the collective topics, number of small speakers participating in collective subjects, list of collective subject launchers, number of collective subject launchers, percentage of dominant speakers launching collective subjects (in relation to the total number of launchers and in relation to the total number of dominant speakers), percentage of collective subjects launched by the dominant speaker, percentage of collective subjects launched by the small speakers.

What he doesn't do: everything else!

**Required environment**

* JAVA 8
* Windows, Mac or Linux platform
* Internet access (for forum messages)

**Download**

 1. Go to [Releases](https://github.com/fredericvergnaud/lame/releases) to download the latest version (jar file)
 2. Double click the jar file to execute L@ME
 
 **Execution**

L@ME is delivered as a JAVA .jar executable file (until better...)

* Execute with the default memory value of the JVM (Java Virtual Machine) set to 256 MB

  Double-click on the file to execute it.
 
* Execution with an increase in the memory of the JVM

  In Console mode, execute the command line :
 
    `java -Xmx512m -jar L@ME_x.x.x.jar`
    
where `512` is the size in MB of the JVM memory and `L@ME_x.x.x.jar` the downloaded L@ME jar file. You can therefore increase it as you wish, depending on the RAM installed on your computer.

 **Usage**
 
 [Go to the wiki](https://github.com/fredericvergnaud/lame/wiki) to see how to use L@ME.

**Love it ?** 

[Tell me](mailto:frederic.vergnaud@mines-paristech.fr) !

**Found a bug ?**

Don’t be afraid to [open an issue](https://github.com/fredericvergnaud/lame/issues).
 
