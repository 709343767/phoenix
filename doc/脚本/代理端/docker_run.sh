docker run -itd -v /tmp:/tmp -v /persistent-phoenix:/persistent-phoenix -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --name phoenix-agent phoenix/phoenix-agent /bin/bash
