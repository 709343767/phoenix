docker run -itd -v /tmp:/tmp -v /persistent-monitoring:/persistent-monitoring -v /etc/localtime:/etc/localtime:ro -p 443:443 --name monitoring-ui monitoring/monitoring-ui /bin/bash
