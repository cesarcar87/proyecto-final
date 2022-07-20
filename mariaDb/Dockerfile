# MariaDB on Alpine Linux
FROM alpine:3.3

# MariaDB server and client
RUN apk update && apk add mariadb mariadb-client
# this is not executed by mariadb package
RUN mysql_install_db --user=mysql

# make life easier
ENV TERM xterm

# demonize
ENTRYPOINT ["mysqld_safe"]

# MariaDB port
EXPOSE 3306
