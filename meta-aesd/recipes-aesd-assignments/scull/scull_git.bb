SUMMARY = "Scull Kernel Module"
DESCRIPTION = "${SUMMARY}"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

inherit module
inherit update-rc.d

INITSCRIPT_NAME = "scull"
INITSCRIPT_PARAMS = "defaults 90"

SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-erpe9416.git;branch=master;protocol=https"
SRC_URI += "file://scull.init"

PV = "1.0+git${SRCPV}"
SRCREV = "9e0e13b46fad63600e30d7900485291523803f7d"

S = "${WORKDIR}/git/scull"

EXTRA_OEMAKE += "M=${S}"

FILES:${PN} += "/lib/modules/${KERNEL_VERSION}/extra/scull.ko"
RPROVIDES:${PN} += "kernel-module-scull-${KERNEL_VERSION}"

do_compile() {
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${S} modules
}

do_install() {
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${S} modules_install INSTALL_MOD_PATH=${D}
    
    # Ensure the directory for kernel modules exists
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra/
    
    # Move the built kernel module to the proper directory
    find ${D} -name "scull.ko" -exec install -m 0644 {} ${D}/lib/modules/${KERNEL_VERSION}/extra/ \;
}

do_install:append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull.init ${D}${sysconfdir}/init.d/scull
}

FILES:${PN} += "/lib/modules/${KERNEL_VERSION}/extra/scull.ko"
FILES:${PN} += "${sysconfdir}/init.d/scull"

RPROVIDES:${PN} += "kernel-module-scull"

