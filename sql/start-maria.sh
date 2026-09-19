# docker run --name mariadb -d -v my.cnf:/etc/mysql/my.cnf -v mariadb:/var/lib/mysql -e MARIADB_USER=user1 -e MARIADB_PASSWORD=change1 -e MARIADB_ROOT_PASSWORD=changeme -p 3306:3306 mariadb:latest
echo starting mariadb service
docker network create mariadb-network 
docker run --detach --network mariadb-network --name mariasvc --env MARIADB_USER=kia --env MARIADB_PASSWORD=changeme --env MARIADB_ROOT_PASSWORD=changeme2  mariadb:latest

echo run below to connect to mariadb service
echo docker run -it --network mariadb-network --rm mariadb mariadb -hmariasvc -uroot -p
