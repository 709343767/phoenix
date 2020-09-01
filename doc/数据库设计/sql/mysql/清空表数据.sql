delete from monitor_instance;
delete from monitor_net;

delete from monitor_server_cpu;
delete from monitor_server_disk;
delete from monitor_server_memory;
delete from monitor_server_netcard;
delete from monitor_server_os;

delete from monitor_jvm_class_loading;
delete from monitor_jvm_garbage_collector;
delete from monitor_jvm_memory;
delete from monitor_jvm_runtime;
delete from monitor_jvm_thread;

alter table monitor_instance auto_increment = 1;
alter table monitor_net auto_increment = 1;

alter table monitor_server_cpu auto_increment = 1;
alter table monitor_server_disk auto_increment = 1;
alter table monitor_server_memory auto_increment = 1;
alter table monitor_server_netcard auto_increment = 1;
alter table monitor_server_os auto_increment = 1;

alter table monitor_jvm_class_loading auto_increment = 1;
alter table monitor_jvm_garbage_collector auto_increment = 1;
alter table monitor_jvm_memory auto_increment = 1;
alter table monitor_jvm_runtime auto_increment = 1;
alter table monitor_jvm_thread auto_increment = 1;