## Setup

### Email

sczone@protonmail.com / MAHK5YWqjqUw6GHm

#Twitter

sczone@protonmail.com / CSF-~C9yBw4bm};h

https://twitter.com/ZoneCredit

### Domain Register
https://www.orangewebsite.com/

sczone@protonmail.com / qGURkvF4Q4JLm2HS

### CentOS

bitlaunch.io / z9XdvypCaPPWPWuj

server password: E9x6vXF8H4pqejGE
server: 165.22.159.111

### Server Setup

#### User `scz`
useradd -m scz
passwd scz    bGGEGHggZ6dbr8qW
usermod -aG wheel scz
sudo adduser scz sudo
su - scz

#### Java

`su - scz`
`sudo yum install java-1.8.0-openjdk-devel`

#### MySql

https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-centos-7
sudo yum update
sudo yum install wget


wget https://dev.mysql.com/get/mysql57-community-release-el7-9.noarch.rpm
sudo rpm -ivh mysql57-community-release-el7-9.noarch.rpm
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl status mysqld
sudo grep 'temporary password' /var/log/mysqld.log  => S?dsHuG_l5zE
sudo mysql_secure_installation

9!m%JRXdmjUF%rSt

##### MySql `credit`

```
uninstall plugin validate_password;
CREATE USER 'maria'@'localhost' IDENTIFIED BY 'maria';
GRANT ALL PRIVILEGES ON *.* TO 'maria'@'localhost';
flush privileges;
```

### Application

application.properties: `server.port=80`

API.js: `baseURL: "http://localhost:80"`

```
cd ~/git/c2
npm run-script build
cp dist/* ~/git/api/application/src/main/resources/static/
-

cd ~/git/api
mvn clean package -DskipTests
-
scp ~/git/api/application/target/application-0.0.1-SNAPSHOT.jar scz@165.22.159.111:
ssh scz@165.22.159.111
ps aux | grep java
kill XXXXX
sudo java -jar application-0.0.1-SNAPSHOT.jar &
```



