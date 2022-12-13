using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.Bases;
using WebAPI.Models.Enums;

namespace WebAPI.Models.Exceptions
{
    public class NothingToReviewException : BaseException
    {
        public NothingToReviewException() : base("Nothing to review")
        {
            _errorCode = ErrorCodeEnum.NoReview;
        }
    }
}