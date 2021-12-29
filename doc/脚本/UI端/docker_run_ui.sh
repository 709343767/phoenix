docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 443:443 --pid host --net host --name phoenix-ui phoenix/phoenix-ui /bin/bash
