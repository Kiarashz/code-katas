docker run --name mariadb -d -v C:\Users\kiara\workspace\tutorials\sql\my.cnf:/etc/mysql/my.cnf -v mariadb:/var/lib/mysql -e MARIADB_USER=user1 -e MARIADB_PASSWORD=change1 -e MARIADB_ROOT_PASSWORD=changeme -p 3306:3306 mariadb:latest