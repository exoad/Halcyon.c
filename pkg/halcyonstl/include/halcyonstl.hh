#pragma once

#ifndef JACKMENG_CC_HALCYONSTL_HEADER
#define JACKMENG_CC_HALCYONSTL_HEADER

namespace h_stl {

#define H_SWAP(type, a, b)  \
  {                         \
    type __SORT_XR_TEMP__;  \
    __SORT_XR_TEMP__ = (b); \
    (b) = (a);              \
    (a) = __SORT_XR_TEMP__; \
  }
}  // namespace h_stl

#endif