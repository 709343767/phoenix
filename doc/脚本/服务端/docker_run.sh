docker run -itd -v /tmp:/tmp -v /persistent-monitoring:/persistent-monitoring -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --name monitoring-server monitoring/monitoring-server /bin/bash
