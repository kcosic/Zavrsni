using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class CarController : BaseController
    {
        public CarController() : base(nameof(CarController)) { }

        [HttpGet]
        [Route("api/Car/{id}")]
        public BaseResponse RetrieveCar(int id, bool expanded = false)
        {
            try
            {
                var car = Db.Cars.Find(id);
                if (car == null || car.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(car.ToDTO(expanded ? 3 : 2));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Car")]
        public BaseResponse RetrieveCars()
        {
            try
            {
                var car = Db.Cars.Where(x => x.UserId == AuthUser.Id && !x.Deleted).ToList();
                if (car == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Car.ToListDTO(car, 2));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/Car/{id}")]
        public BaseResponse DeleteCar(int id)
        {
            try
            {
                var car = Db.Cars.Find(id);
                if (car == null || car.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                car.DateDeleted = DateTime.Now;
                car.Deleted = true;

                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Car")]
        public BaseResponse CreateCar(Car newCarDTO)
        {
            try
            {
                if (newCarDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newCar = new Car
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Manufacturer = newCarDTO.Manufacturer,
                    Model= newCarDTO.Model,
                    Odometer= newCarDTO.Odometer,
                    Year = newCarDTO.Year,
                    UserId = AuthUser.Id,
                    LicensePlate = newCarDTO.LicensePlate
                };

                Db.Cars.Add(newCar);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Car/{id}")]
        public BaseResponse UpdateAppointment([FromUri] int id, [FromBody] Car carDTO)
        {
            try
            {
                if (carDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var car = Db.Cars.Find(id);
                if (car == null || car.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                car.DateModified = DateTime.Now;
                car.Manufacturer = carDTO.Manufacturer;
                car.Model = carDTO.Model;
                car.Odometer = carDTO.Odometer;
                car.Year = carDTO.Year;
                car.LicensePlate = carDTO.LicensePlate;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}