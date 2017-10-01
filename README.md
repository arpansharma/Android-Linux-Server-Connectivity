# Android-Linux-Server-Connectivity
Connecting Android app to MySql Database on a linux server

# Instaling a LAMP Server

## 1. Install Apache
To install Apache you must install the Metapackage apache2. This can be done by searching for and installing in the Software Centre, or by running the following command.

### sudo apt-get install apache2

## 2. Install MySQL
To install MySQL you must install the Metapackage mysql-server. This can be done by searching for and installing in the Software Centre, or by running the following command.

### sudo apt-get install mysql-server

## 3. Install PHP
To install PHP you must install the Metapackages php5 and libapache2-mod-php5. This can be done by searching for and installing in the Software Centre, or by running the following command.

### sudo apt-get install php5 libapache2-mod-php5

## 4. Restart Server
Your server should restart Apache automatically after the installation of both MySQL and PHP. If it doesn't, execute this command.

### sudo /etc/init.d/apache2 restart

## 5. Check Apache
Open a web browser and navigate to http://localhost/. You should see a message saying It works!

## 6. Check PHP
You can check your PHP by executing any PHP file from within /var/www/. Alternatively you can execute the following command, which will make PHP run the code without the need for creating a file .

### php -r 'echo "\n\nYour PHP installation is working fine.\n\n\n";'


# Instaling viretualenv and MySQL-python

### 1. apt-get install python-pip

### 2. pip install virtualenv

### 3. apt-get install libmysqlclient-dev

### 4. virtualenv Database_Connect

### 5. source Database_Connect/bin/activate

### 6. pip install MySQL-python

### 7. deactivate

