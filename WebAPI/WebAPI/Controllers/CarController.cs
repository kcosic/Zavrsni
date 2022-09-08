using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class CarController : BaseController
    {
        public CarController() : base(nameof(CarController)) { }

        [HttpGet]
        [Route("api/Car/{id}")]
        public BaseResponse RetrieveCar(string id)
        {
            try
            {
                var car = Db.Cars.Find(id);
                if (car == null || car.Deleted)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(car.ToDTO());
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        //[HttpGet]
        //[Route("api/Car")]
        //public BaseResponse RetrieveCars()
        //{
        //    try
        //    {
        //        var car = Db.Cars.Where(x => x.ShopId == AuthShop.Id && !x.Deleted).ToList();
        //        if (car == null)
        //        {
        //            throw new Exception("Record not found");
        //        }

        //        return CreateOkResponse(Models.ORM.Car.ToListDTO(car));
        //    }
        //    catch (Exception e)
        //    {
        //        return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
        //    }

        //}

        [HttpDelete]
        [Route("api/Car/{id}")]
        public BaseResponse DeleteCar(string id)
        {
            try
            {
                var car = Db.Cars.Find(id);
                if (car == null || car.Deleted)
                {
                    throw new Exception("Record not found");
                }
                car.DateDeleted = DateTime.Now;
                car.Deleted = true;

                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Car")]
        public BaseResponse CreateCar(Models.ORM.Car newCarDTO)
        {
            try
            {
                throw new NotImplementedException();
                if (newCarDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newCar = new Models.ORM.Car
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Make = newCarDTO.Make,
                    Manufacturer = newCarDTO.Manufacturer,
                    Model= newCarDTO.Model,
                    Odometer= newCarDTO.Odometer,
                    Year = newCarDTO.Year
                };

                Db.Cars.Add(newCar);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Car/{id}")]
        public BaseResponse UpdateAppointment([FromUri] string id, [FromBody] Models.ORM.Car carDTO)
        {
            try
            {
                if (id == null || carDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var car = Db.Cars.Find(id);
                car.DateModified = DateTime.Now;
                car.DateCreated = DateTime.Now;
                car.DateModified = DateTime.Now;
                car.Make = carDTO.Make;
                car.Manufacturer = carDTO.Manufacturer;
                car.Model = carDTO.Model;
                car.Odometer = carDTO.Odometer;
                car.Year = carDTO.Year;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}