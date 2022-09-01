using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.Exceptions
{
    public class LocationNotFoundException : Exception
    {
        public LocationNotFoundException() : base("Unable to find location from provided parameter") 
        {

        }
    }
}