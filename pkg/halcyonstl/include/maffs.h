#pragma once

#ifndef JACKMENG_CC_MAFFS_HEADER
#define JACKMENG_CC_MAFFS_HEADER

#define HALCYON_MAFFS_PI 3.14159265359
#define HALCYON_MAFFS_BIT_1 1
#define HALCYON_MAFFS_BIT_2 2
#define HALCYON_MAFFS_BIT_5 16
#define HALCYON_MAFFS_BIT_18 131072
#define HALCYON_MAFFS_RBIT_MASK (HALCYON_MAFFS_BIT_1 + HALCYON_MAFFS_BIT_2 + HALCYON_MAFFS_BIT_5 + HALCYON_MAFFS_BIT_18)

namespace halcyon_maffs {
/*--------------------------------------- /
/ implemented in the standard prim_math.o /
/----------------------------------------*/
float ran_1(long*);
int ran_bit_1(unsigned long*);
int ran_bit_2(unsigned long*);

}  // namespace halcyon_maffs

#endif