SUMMARY = "Misc Modules Kernel Modules"
DESCRIPTION = "${SUMMARY}"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module

inherit update-rc.d

INITSCRIPT_NAME = "misc-modules"
INITSCRIPT_PARAMS = "defaults 90"


SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-erpe9416.git;branch=master"
SRC_URI += "file://misc-modules.init"


# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "9e0e13b46fad63600e30d7900485291523803f7d"

S = "${WORKDIR}/git/misc-modules"

EXTRA_OEMAKE += " -C ${STAGING_KERNEL_DIR} M=${S}"

FILES:${PN} += "/lib/modules/${KERNEL_VERSION}/extra/modules.ko"


do_install() {
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${S} modules_install INSTALL_MOD_PATH=${D}
}


do_compile() {
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${WORKDIR}/git/misc-modules modules
}

do_install:append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/misc-modules.init ${D}${sysconfdir}/init.d/misc-modules
}

FILES:${PN} += "/lib/modules/${KERNEL_VERSION}/extra/misc-modules.ko"
FILES:${PN} += "${sysconfdir}/init.d/misc-modules"

RPROVIDES:${PN} += "kernel-module-misc-modules"

