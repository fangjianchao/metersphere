# 插入测试数据
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId1', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目1', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId2', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目2', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId3', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目3', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId4', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目4', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId5', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目5', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);
INSERT INTO project (id, num, organization_id, name, description, create_user, update_user, create_time, update_time) VALUES ('projectId6', null, (SELECT id FROM organization WHERE name LIKE '默认组织'), '默认项目6', '系统默认创建的项目', 'admin', 'admin', unix_timestamp() * 1000, unix_timestamp() * 1000);