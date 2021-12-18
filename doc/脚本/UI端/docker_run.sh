docker run -itd -v /tmp:/tmp -v /persistent-phoenix:/persistent-phoenix -v /etc/localtime:/etc/localtime:ro -p 443:443 --name phoenix-ui phoenix/phoenix-ui /bin/bash
