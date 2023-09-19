
cd ~/code/LifeGuard-Backend || echo "did not cd (line: 2)"
#remove distributions
cd build/distributions || echo "did not cd (line 4)"

# shellcheck disable=SC2144
if [ -f *.zip ]; then rm -r *.zip; else echo "did not remove .zip(s)"; fi
#find . -name "*.zip" -type f -delete


# shellcheck disable=SC2144
if [ -f *.tar ]; then rm -r *.tar; else echo "did not remove .tar(s)"; fi
