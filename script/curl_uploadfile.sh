cd $(dirname $0)
curl -F "file=@./mysecretfile.txt" http://localhost:8080/upload