Library Book Management is a search tool to search a database of Library Books. Upon running the program should open a GUI that allows the user
to search for books. Database is Automatically created and filled with an import from kagglehub.dataset_download("jealousleopard/goodreadsbooks").

I ran out of time (this was an assignment) and was not able to finish ALL of my functionality (such as searching by ISBN) but the core functionality 
of the program is operable. You should be able to search by Book Name or Author and Recommend Something will randomly recommend a book to you.


REQUIREMENTS: 

Minimum Requirements:
Python 3.9.6
Gradle 8.10
(PostgreSQL) 14.15
Java 17+

Make Sure PostgreSQL is started before running:

Windows Powershell - net start postgresql

MacOS(HomeBrew) - brew services start postgresql

Linux - sudo systemctl start postgresql

RUNNING THE PROGRAM: Navigate to the Project Folder in Command Line or Terminal.

Windows:
.\gradlew.bat clean
.\gradlew.bat build
.\gradlew.bat run

MacOS & Linux: 
./gradlew clean
./gradlew build
./gradlew run

IF POSTGRESQL REQUIRES A PASSWORD:
My version of PostgresSQL didnt seem to require Authentication for my program to run, but my understanding is that this can depend
on what version you installed and how you installed it. I installed using HomeBrew on MacOS.

If you have Authentication issues check DatabaseManagement.java, data_import.py, and BookDAO.java anywhere that you see "yourpassword" you should be
able to replace with your PostgreSQL password. Likewise if you didnt use the default PostgreSQL username you may need to change that as well.

NOTES ON DEPENDENICES:
Dependencies Should Automatically Install when you run the Progam, but if they dont:

Navigate to the project folder in Command Line or Terminal

Windows: 
python -m pip install --upgrade pip
pip install -r src/main/resources/requirements.txt

MacOS:
python3 -m pip install --upgrade pip
pip3 install -r src/main/resources/requirements.txt

Linux: 
python3 -m pip install --upgrade pip
pip3 install -r src/main/resources/requirements.txt

and if for some reason that doesnt work, open requirements.txt and just look up how to manually download the dependencies on google.








