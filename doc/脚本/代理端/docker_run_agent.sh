docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --name phoenix-agent phoenix/phoenix-agent /bin/bash
