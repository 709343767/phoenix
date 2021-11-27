docker run -itd -v /tmp:/tmp -v /persistent-monitoring:/persistent-monitoring -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --name monitoring-agent monitoring/monitoring-agent /bin/bash
