# FTPServer
Реализация простого FTP сервера согласно спецификациям RFC 959 и ее расширения RFС 2428. Поддерживается только анонимная аутентификация и команды пользователя SEND (STOR), GET (RETR) и BYE. Работает только в активном режиме передачи данных (клиент и сервер не должны находиться за NAT-ом).
