cd $(dirname $0)
rm ./download/mysecretfile-1.txt
curl "http://localhost:8080/download/mysecretfile.txt" --output "download/mysecretfile-1.txt"