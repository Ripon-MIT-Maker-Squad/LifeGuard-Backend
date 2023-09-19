ssh -i /Users/lukebair/code/LifeGuard-Backend/ssh-key-k3s-server.key ubuntu@129.146.23.77<<EOF
sudo systemctl stop lifeGuard
EOF

./clearDistributions.sh

cd ../..
./gradlew assemble
cd src/shell || echo "Something dumb happened"

./sftp.sh

ssh -i /Users/lukebair/code/LifeGuard-Backend/ssh-key-k3s-server.key ubuntu@129.146.23.77<<EOF
sudo systemctl restart lifeGuard
EOF