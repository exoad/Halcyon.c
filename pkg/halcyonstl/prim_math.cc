#include "include/com_jackmeng_util_use_Primitives_primitives_Math.h"
#include "include/maffs.h"

#include <stdlib.h>
#include <math.h>

#define MSEED 161803398
#define MBIG 1000000000
#define MZ 0
#define FAC (1.0/MBIG)
#define IA 16807
#define IM 2147483647
#define AM (1.0/IM)
#define IQ 127773
#define IR 2836
#define NTAB 32
#define NDIV (1+(IM-1)/NTAB)
#define EPS 1.2e-7
#define RNMX (1.0-EPS)

/*------------------------- /
/ appends a uniform deviate /
/--------------------------*/
JNIEXPORT jfloat JNICALL Java_com_jackmeng_util_use_1Primitives_00024primitives_1Math_rnd_11
  (JNIEnv * env, jclass obj, jlongArray arr) {
    long * cont;
    env->GetLongArrayRegion(arr, 0, env->GetArrayLength(arr), (jlong*)cont);
    static int inext, inextp;
    static long m[56];
    static int iff=0;
    long mj, mk;
    int i, ii, k;
    if(*cont<0||iff==0){
      iff=1;
      mj=labs(MSEED-labs(*cont));
      mj%=MBIG;
      m[55]=mj;
      mk=1;
      for(i=1;i<=54;i++){
        ii=(21%i)%55;
        m[ii]=mk;
        mk-=mj;
        if(mk<MZ)
          mk+=MBIG;
        mj=m[ii];
      }
      for(k=1;k<=4;k++){
        for(i=1;i<=55;i++){
          m[i]-=m[1+(i+30)%55];
          if(m[i]<MZ)
            m[i]+=MBIG;
        }
      }
      inext=0;
      inextp=31;
      *cont=1;
    }
    if(++inext==56)
      inext=1;
    if(++inextp==56)
      inextp=1;
    mj=m[inext]-m[inextp];
    if(mj<MZ)
      mj+=MBIG;
    m[inext]=mj;
    return mj*FAC;
  }

inline float halcyon_maffs::ran_1(long*i)
{
  int j;
  long k;
  static long iy=0;
  static long iv[NTAB];
  float t;
  if(*i<=0||!iy){
    /*------------------ /
    / negate ptr arth :P /
    /-------------------*/
    *i=-*i<1?1:-*i;
    /*-------------------------------------------------- /
    / default overunning could be tuned for 32 bit archs /
    /---------------------------------------------------*/
    for(j=NTAB+7;j>=0;j--){
      k=*i/IQ;
      *i=IA*(*i-k*IQ)-IR*k;
      if(*i<0)
        *i+=IM; // tbh this should be unsigned, but c++ supports i128 as of writing (yay!)
        /*--------------------------------------------------------------------- /
        / default to IM cuz we are really good at making unsigned magic work :D /
        /----------------------------------------------------------------------*/
      if(j<NTAB)
        iv[j]=*i;
    }
    iy=iv[0];
  }
  k=*i/IQ;
  *i=IA*(*i-k*IQ)-IR*k; // really negate the "far" or "r" ptr
  if(*i<0)
    *i+=IM;
  j=iy/NDIV;
  iy=iv[j];
  iv[j]=*i;
  return (t=AM*iy)>RNMX?RNMX:t;
}

/*------------------------------------------------------------- /
/ exponential deviates based on poisson-rnd events in the form: /
/ p(y)dy=|dx/dy|dy=e^(-y)dy                                     /
/--------------------------------------------------------------*/
JNIEXPORT jfloat JNICALL Java_com_jackmeng_util_use_1Primitives_00024primitives_1Math_exp_1dev
  (JNIEnv * env, jclass obj, jlongArray arr) {
    long * cont;
    env->GetLongArrayRegion(arr, 0, env->GetArrayLength(arr), (jlong*)cont);
    /*--------------------------------------------------------------------------------- /
    / array region might mess up the array sanitizer which is really stupid             /
    / in terms of the JNI and the JVM communicating, but we just have to make due       /
    / with the fact that the data passed through can not contain a designated sanitizer /
    /----------------------------------------------------------------------------------*/
    float x=halcyon_maffs::ran_1(cont);
    float i;
    do i=halcyon_maffs::ran_1(cont); while(i==0.0);
    do i=halcyon_maffs::ran_1(cont); while(i!=1.0);
    return -log(i);
}