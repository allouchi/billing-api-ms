-- =========================================================================
-- V2 — Super administrator role + seeded super-admin account
-- =========================================================================
-- Adds a dedicated ROLE_SUPER_ADMIN and a super-admin user for the deployed
-- environment. The password is a strong, deployment-specific secret (BCrypt).
-- Change it after first login.

INSERT INTO `t_role` (`role_name`, `description`)
SELECT 'ROLE_SUPER_ADMIN', 'Super administrateur'
WHERE NOT EXISTS (SELECT 1 FROM `t_role` WHERE `role_name` = 'ROLE_SUPER_ADMIN');

-- Super-admin account (activated). BCrypt hash of the deployment super-admin password.
INSERT INTO `t_user` (`email`, `first_name`, `last_name`, `password`, `siret`, `activated`, `language`)
SELECT 'admin@fact.iacsas.org', 'Super', 'Admin',
       '$2a$10$Oq2IJS9BIWRVAQPxWAZJdewUnd/LcYqW61wfaDnWQN.WnB8mcPoMC',
       '85292702900011', true, 'fr'
WHERE NOT EXISTS (SELECT 1 FROM `t_user` WHERE `email` = 'admin@fact.iacsas.org');

-- Grant the super-admin every role (super-admin, admin, consult, edit).
INSERT INTO `user_roles` (`user_id`, `role_id`)
SELECT u.id, r.id
FROM `t_user` u
JOIN `t_role` r ON r.role_name IN ('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_CONSULT', 'ROLE_EDIT')
WHERE u.email = 'admin@fact.iacsas.org'
  AND NOT EXISTS (
    SELECT 1 FROM `user_roles` ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
