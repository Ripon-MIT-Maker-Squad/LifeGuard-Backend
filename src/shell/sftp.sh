export PATH=$PATH:~/code/PoolGuard-Backend

ssh -i /Users/lukebair/code/PoolGuard-Backend/ssh-key-k3s-server.key ubuntu@129.146.23.77 <<EOF
#remove previously saved version
cd poolGuard/safetynet
if [ -f *.zip ] || [ -d * ]; then rm -r *; else echo "Nothing to delete"; fi
cd ..

#if .zip exits, then move it into the safteynet
if [ -f *.zip ]; then mv *.zip safetynet; else echo "did not delete zip"; fi

#remove the unzipped file for memory saving reasons???
if [ -d PoolGuard-Backend-* ]; then rm -r PoolGuard-Backend-*; else echo "did not delete previous dir"; fi
exit
EOF

sftp -i /Users/lukebair/code/PoolGuard-Backend/ssh-key-k3s-server.key ubuntu@129.146.23.77 <<EOF
lcd ../../build/distributions
cd poolGuard
put *.zip
exit
EOF

ssh -i /Users/lukebair/code/PoolGuard-Backend/ssh-key-k3s-server.key ubuntu@129.146.23.77 <<EOF
cd poolGuard
if [ -f *.zip ]; then unzip *.zip; else echo "Something dumb happened"; fi #breaks
exit
EOF

##### FILE SETUP COMPLETE #####

