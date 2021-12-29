docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --name phoenix-server phoenix/phoenix-server /bin/bash
