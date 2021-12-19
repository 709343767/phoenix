docker run -itd -v /tmp:/tmp -v /persistent-phoenix:/persistent-phoenix -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --name phoenix-server phoenix/phoenix-server /bin/bash
