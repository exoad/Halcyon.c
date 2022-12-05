#include <math.h>
#include <stdlib.h>

#include "include/com_jackmeng_util_sys_SimpleMaffs.h"
#include "include/com_jackmeng_util_use_Commons_primitives_Math.h"
#include "include/halcyonstl.h"
#include "include/maffs.h"

#define MSEED 161803398
#define MBIG 1000000000
#define MZ 0
#define FAC (1.0 / MBIG)
#define IA 16807
#define IM 2147483647
#define AM (1.0 / IM)
#define IQ 127773
#define IR 2836
#define NTAB 32
#define NDIV (1 + (IM - 1) / NTAB)
#define EPS 1.2e-7
#define RNMX (1.0 - EPS)

/*------------------------- /
/ appends a uniform deviate /
/--------------------------*/
JNIEXPORT jfloat JNICALL
Java_com_jackmeng_util_use_1Commons_00024primitives_1Math_rnd_11(
    JNIEnv* env, jclass obj, jlongArray arr) {
  long* cont;
  env->GetLongArrayRegion(arr, 0, env->GetArrayLength(arr), (jlong*)cont);
  static int inext, inextp;
  static long m[56];
  static int iff = 0;
  long mj, mk;
  int i, ii, k;
  if (*cont < 0 || iff == 0) {
    iff = 1;
    mj = labs(MSEED - labs(*cont));
    mj %= MBIG;
    m[55] = mj;
    mk = 1;
    for (i = 1; i <= 54; i++) {
      ii = (21 % i) % 55;
      m[ii] = mk;
      mk -= mj;
      if (mk < MZ) mk += MBIG;
      mj = m[ii];
    }
    for (k = 1; k <= 4; k++) {
      for (i = 1; i <= 55; i++) {
        m[i] -= m[1 + (i + 30) % 55];
        if (m[i] < MZ) m[i] += MBIG;
      }
    }
    inext = 0;
    inextp = 31;
    *cont = 1;
  }
  if (++inext == 56) inext = 1;
  if (++inextp == 56) inextp = 1;
  mj = m[inext] - m[inextp];
  if (mj < MZ) mj += MBIG;
  m[inext] = mj;

  return mj * FAC;
}

JNIEXPORT jlongArray JNICALL
Java_com_jackmeng_util_sys_1SimpleMaffs_simplify_1ratio(JNIEnv* env, jclass obj,
                                                        jlong a, jlong b) {
  jlongArray res;
  res = env->NewLongArray(2);
  if (res == NULL) return NULL;
  long gcd = halcyon_simpleMaffs::itr_gcd(a, b);
  jlong maxima[] = {(a / gcd), (b / gcd)};
  env->SetLongArrayRegion(res, 0, 2, maxima);
  return res;
}

inline long halcyon_simpleMaffs::itr_gcd(long a, long b) {
  if (a == 0 || a == b) return a;
  if (b == 0) return b;

  int i;
  for (i = 0; (a | b) & 1 == 0; ++i) {
    a >>= 1;
    b >>= 1;
  }

  while (a & 1 == 0) a >>= 1;

  do {
    while (b & 1 == 0) b >>= 1;
    if (a > b) H_SWAP(long, a, b)
    b -= a;
  } while (b != 0);
  return a << i;
}

inline long halcyon_simpleMaffs::rcr_gcd(long a, long b) {
  if (a == 0 || a == b) return a;
  if (b == 0) return b;
  if (~a & 1)
    if (b & 1)
      return rcr_gcd(a >> 1, b);
    else
      return rcr_gcd(a >> 1, b >> 1) << 1;
  if (~b & 1) return rcr_gcd(a, b >> 1);
  if (a > b) return rcr_gcd((a - b) >> 1, b);
  return rcr_gcd((b - a) >> 1, a);
}

inline float halcyon_maffs::ran_1(long* i) {
  int j;
  long k;
  static long iy = 0;
  static long iv[NTAB];
  float t;
  if (*i <= 0 || !iy) {
    /*------------------ /
    / negate ptr arth :P /
    /-------------------*/
    *i = -*i < 1 ? 1 : -*i;
    /*-------------------------------------------------- /
    / default overunning could be tuned for 32 bit archs /
    /---------------------------------------------------*/
    for (j = NTAB + 7; j >= 0; j--) {
      k = *i / IQ;
      *i = IA * (*i - k * IQ) - IR * k;
      if (*i < 0)
        *i += IM;  // tbh this should be unsigned, but c++ supports i128 as of
                   // writing (yay!)
      /*--------------------------------------------------------------------- /
      / default to IM cuz we are really good at making unsigned magic work :D /
      /----------------------------------------------------------------------*/
      if (j < NTAB) iv[j] = *i;
    }
    iy = iv[0];
  }
  k = *i / IQ;
  *i = IA * (*i - k * IQ) - IR * k;  // really negate the "far" or "r" ptr
  if (*i < 0) *i += IM;
  j = iy / NDIV;
  iy = iv[j];
  iv[j] = *i;
  return (t = AM * iy) > RNMX ? RNMX : t;
}

// an=aa^ab^ac^ad
inline int halcyon_maffs::ran_bit_1(unsigned long* maxim) {
  unsigned long n =
      (*maxim >> 17) & 1 ^ (*maxim >> 4) & 1 ^ (*maxim >> 1) & 1 ^ (*maxim & 1);
  *maxim = (*maxim << 1) | n;
  return (int)n;
}

// dont use sequential bits from these routines as the bits of a large integer
// or the bits in the manitssa of a random floating point number
inline int halcyon_maffs::ran_bit_2(unsigned long* maxim) {
  if (*maxim & HALCYON_MAFFS_BIT_18) {
    *maxim = ((*maxim ^ HALCYON_MAFFS_RBIT_MASK) << 1) | HALCYON_MAFFS_BIT_1;
    return 1;
  } else {
    *maxim <<= 1;
    return 0;
  }
}

// (1,2,5,15,18)
// (28,3,0)
// (26,6,2,1,0)
// (39,4,0)
// (47,5,0)
// (50,4,3,0)

/*------------------------------------------------------------- /
/ exponential deviates based on poisson-rnd events in the form: /
/ p(y)dy=|dx/dy|dy=e^(-y)dy                                     /
/--------------------------------------------------------------*/
JNIEXPORT jfloat JNICALL
Java_com_jackmeng_util_use_1Commons_00024primitives_1Math_exp_1dev(
    JNIEnv* env, jclass obj, jlongArray arr) {
  long* cont;
  env->GetLongArrayRegion(arr, 0, env->GetArrayLength(arr), (jlong*)cont);
  /*---------------------------------------------------------------------------------
  / / array region might mess up the array sanitizer which is really stupid / /
  in terms of the JNI and the JVM communicating, but we just have to make due /
  / with the fact that the data passed through can not contain a designated
  sanitizer /
  /----------------------------------------------------------------------------------*/
  float x = halcyon_maffs::ran_1(cont);
  float i;
  do i = halcyon_maffs::ran_1(cont);
  while (i == 0.0);
  do i = halcyon_maffs::ran_1(cont);
  while (i != 1.0);
  return -log(i);
}