locals {
  preview_vault_name     = join("-", [var.raw_product, "aat"])
  non_preview_vault_name = join("-", [var.raw_product, var.env])
  key_vault_name         = var.env == "preview" || var.env == "spreview" ? local.preview_vault_name : local.non_preview_vault_name

  s2s_rg_prefix            = "rpe-service-auth-provider"
  s2s_key_vault_name       = var.env == "preview" || var.env == "spreview" ? join("-", ["s2s", "aat"]) : join("-", ["s2s", var.env])
  s2s_vault_resource_group = var.env == "preview" || var.env == "spreview" ? join("-", [local.s2s_rg_prefix, "aat"]) : join("-", [local.s2s_rg_prefix, var.env])
  db_name                  = join("-", [var.product-v16, var.component-v16])
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
  name         = "microservicekey-rd-location-ref-api"
  key_vault_id = data.azurerm_key_vault.s2s_key_vault.id
}

resource "azurerm_key_vault_secret" "location_s2s_secret" {
  name         = "location-ref-api-s2s-secret"
  value        = data.azurerm_key_vault_secret.s2s_secret.value
  key_vault_id = data.azurerm_key_vault.rd_key_vault.id
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
      report_privilege_schema : "locrefdata"
      report_privilege_tables : ["SERVICE_TO_CCD_CASE_TYPE_ASSOC", "building_location", "court_venue", "region", "cluster", "court_type", "court_type_service_assoc", "dataload_schedular_audit", "dataload_exception_records"]
    }
  ]

  # Setup Access Reader db user
  force_user_permissions_trigger = "3"

  # Sets correct DB owner after migration to fix permissions
  enable_schema_ownership        = var.enable_schema_ownership
  force_schema_ownership_trigger = "3"
  kv_subscription                = var.kv_subscription
  kv_name                        = data.azurerm_key_vault.rd_key_vault.name
  user_secret_name               = azurerm_key_vault_secret.POSTGRES-USER.name
  pass_secret_name               = azurerm_key_vault_secret.POSTGRES-PASS.name

  subnet_suffix = "expanded"
  pgsql_version = "16"
  pgsql_sku     = var.pgsql_sku
  product       = "rd"
  name          = local.db_name

  pgsql_server_configuration     = var.pgsql_server_configuration
  action_group_name              = join("-", [var.action_group_name, local.db_name, "replica", var.env])
  email_address_key              = var.email_address_key
  email_address_key_vault_id     = data.azurerm_key_vault.rd_key_vault.id

  # Reporting
  enable_db_reporting_privileges = true
  force_db_report_privileges_trigger = "1"
}

resource "azurerm_key_vault_secret" "POSTGRES-HOST" {
  name         = join("-", [var.component, "POSTGRES-HOST"])
  value        = module.db-rd-location-ref-api-v16.fqdn
  key_vault_id = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-DATABASE" {
  name         = join("-", [var.component, "POSTGRES-DATABASE"])
  value        = "dbrdlocationref"
  key_vault_id = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name         = join("-", [var.component, "POSTGRES-PORT"])
  value        = "5432"
  key_vault_id = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name         = join("-", [var.component, "POSTGRES-USER"])
  value        = module.db-rd-location-ref-api-v16.username
  key_vault_id = data.azurerm_key_vault.rd_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name         = join("-", [var.component, "POSTGRES-PASS"])
  value        = module.db-rd-location-ref-api-v16.password
  key_vault_id = data.azurerm_key_vault.rd_key_vault.id
}
