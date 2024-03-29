locals {
  preview_vault_name      = join("-", [var.raw_product, "aat"])
  non_preview_vault_name  = join("-", [var.raw_product, var.env])
  key_vault_name          = var.env == "preview" || var.env == "spreview" ? local.preview_vault_name : local.non_preview_vault_name

  s2s_rg_prefix               = "rpe-service-auth-provider"
  s2s_key_vault_name          = var.env == "preview" || var.env == "spreview" ? join("-", ["s2s", "aat"]) : join("-", ["s2s", var.env])
  s2s_vault_resource_group    = var.env == "preview" || var.env == "spreview" ? join("-", [local.s2s_rg_prefix, "aat"]) : join("-", [local.s2s_rg_prefix, var.env])
}

data "azurerm_key_vault" "rd_key_vault" {
  name                = local.key_vault_name
  resource_group_name = local.key_vault_name
}

data "azurerm_key_vault" "s2s_key_vault" {
  name                = local.s2s_key_vault_name
  resource_group_name = local.s2s_vault_resource_group
}

data "azurerm_key_vault_secret" "s2s_secret" {
  name          = "microservicekey-rd-location-ref-api"
  key_vault_id  = data.azurerm_key_vault.s2s_key_vault.id
}

resource "azurerm_key_vault_secret" "location_s2s_secret" {
  name          = "location-ref-api-s2s-secret"
  value         = data.azurerm_key_vault_secret.s2s_secret.value
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-HOST" {
  name          = join("-", [var.component, "POSTGRES-HOST"])
  value         = module.db-rd-location-ref-api.host_name
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-DATABASE" {
  name          = join("-", [var.component, "POSTGRES-DATABASE"])
  value         = module.db-rd-location-ref-api.postgresql_database
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name          = join("-", [var.component, "POSTGRES-PORT"])
  value         = "5432"
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name          = join("-", [var.component, "POSTGRES-USER"])
  value         = module.db-rd-location-ref-api.user_name
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name          = join("-", [var.component, "POSTGRES-PASS"])
  value         = module.db-rd-location-ref-api.postgresql_password
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

module "db-rd-location-ref-api" {
  source              = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  name                = join("-", [var.product, var.component, "postgres-db"])
  product             = var.product
  component           = var.component
  location            = var.location
  subscription        = var.subscription
  env                 = var.env
  postgresql_user     = "dbrdlocationref"
  database_name       = "dbrdlocationref"
  common_tags         = var.common_tags
  postgresql_version  = var.postgresql_version
  replicas            = var.db_replicas
}


# Create the database server v16
# Name and resource group name will be defaults (<product>-<component>-<env> and <product>-<component>-data-<env> respectively)
module "db-rd-location-ref-api-v16" {
  source = "git@github.com:hmcts/terraform-module-postgresql-flexible?ref=master"

  providers = {
    azurerm.postgres_network = azurerm.postgres_network
  }

  admin_user_object_id = var.jenkins_AAD_objectId
  business_area        = "cft"
  common_tags          = var.common_tags
  component            = var.component-v16
  env                  = var.env
  pgsql_databases = [
    {
      name = "dbrdlocationref"
    }
  ]

  # Setup Access Reader db user
  force_user_permissions_trigger = "1"


  # Sets correct DB owner after migration to fix permissions
  enable_schema_ownership = var.enable_schema_ownership
  force_schema_ownership_trigger = "1"
  kv_subscription = var.kv_subscription
  kv_name = data.azurerm_key_vault.rd_key_vault.name
  user_secret_name = azurerm_key_vault_secret.POSTGRES-USER.name
  pass_secret_name = azurerm_key_vault_secret.POSTGRES-PASS.name

  subnet_suffix        = "expanded"
  pgsql_version        = "16"
  product              = var.product-v16
  name               = join("-", [var.product-v16, var.component-v16])
}

resource "azurerm_key_vault_secret" "POSTGRES-HOST-v16" {
  name          = join("-", [var.component, "POSTGRES-HOST-v16"])
  value         = module.db-rd-location-ref-api-v16.fqdn
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-DATABASE-v16" {
  name          = join("-", [var.component, "POSTGRES-DATABASE-v16"])
  value         = "dbrdlocationref"
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT-v16" {
  name          = join("-", [var.component, "POSTGRES-PORT-v16"])
  value         = "5432"
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-USER-v16" {
  name          = join("-", [var.component, "POSTGRES-USER-v16"])
  value         = module.db-rd-location-ref-api-v16.username
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS-v16" {
  name          = join("-", [var.component, "POSTGRES-PASS-v16"])
  value         = module.db-rd-location-ref-api-v16.password
  key_vault_id  = data.azurerm_key_vault.rd_key_vault.id
}
